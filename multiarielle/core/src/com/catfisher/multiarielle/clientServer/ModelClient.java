package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientChatEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.*;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;

@Log4j2
public class ModelClient implements ServerEventVisitor<Boolean> {
    private final AbsoluteModel localCopy;
    private ProxyServer server;
    @Getter
    private final String clientId;

    @Getter
    private final AtomicBoolean isServerReady = new AtomicBoolean(false);

    private Map<Long, Delta> unacknowledgedDeltas = new HashMap<>();

    private final AtomicLong sequenceNumber = new AtomicLong(0);

    public ModelClient(AbsoluteModel localCopy) {
        this.localCopy = localCopy;
        this.clientId = UUID.randomUUID().toString();
    }

    public void associateServer(ProxyServer server) {
        this.server = server;
        server.setupClient(this);
        server.receive(new ConnectEvent(this.clientId, null));
    }

    public Boolean consume(ServerEvent e) {
        return e.receive(this);
    }

    @Override
    public Boolean visit(ServerDeltaEvent e) {
        log.info("Client received from server event {}", e);
        return localCopy.consume(e.getDelta());
    }

    @Override
    public Boolean visit(SynchronizeEvent e) {
        log.info("Client received from server event {}", e);
        localCopy.synchronize(e);

        // Now replay all of the events that happened after the sync
        Map<Long, Delta> newUnacknowledgedDeltas = new HashMap<>();
        long sequenceNumberWatermark = e.getSequenceNumberWatermark();
        for (Map.Entry<Long, Delta> candidateDelta : unacknowledgedDeltas.entrySet()) {
            if (candidateDelta.getKey() > sequenceNumberWatermark) {
                localCopy.consume(candidateDelta.getValue());
                newUnacknowledgedDeltas.put(candidateDelta.getKey(), candidateDelta.getValue());
            }
        }
        unacknowledgedDeltas = newUnacknowledgedDeltas;
        return true;
    }

    @Override
    public Boolean visit(ServerConnectionAcknowledged e) {
        log.info("Received ack {}", e);
        synchronized (isServerReady) {
            isServerReady.set(true);
            isServerReady.notifyAll();
        }
        return true;
    }

    @Override
    public Boolean visit(ServerRejectDeltaEvent e) {
        for (Map.Entry<Long, Delta> delta : unacknowledgedDeltas.entrySet()) {
            if (delta.getKey().equals(e.getSequenceId())) {
                Delta inverted = delta.getValue().invert();
                if (inverted != null) {
                    return localCopy.consume(inverted);
                }
            }
        }
        return true;
    }

    public void waitForServerReady() throws InterruptedException {
        log.info("Waiting for server to acknowledge");
        synchronized (isServerReady) {
            while (!isServerReady.get()) {
                isServerReady.wait();
            }
        }
        log.info("Server ready");
    }

    public boolean forwardDeltaToServer(Delta delta) {
        long thisSequenceNumber = sequenceNumber.incrementAndGet();
        unacknowledgedDeltas.put(thisSequenceNumber, delta);
        return server.receive(new ClientDeltaEvent(clientId, thisSequenceNumber, delta));
    }

    public boolean forwardChatToServer(String message) {
        return server.receive(new ClientChatEvent(clientId, message));
    }
}
