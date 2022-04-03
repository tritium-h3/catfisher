package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerEventVisitor;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class ModelClient implements ServerEventVisitor<Boolean> {
    private final AbsoluteModel localCopy;
    private ProxyServer server;
    @Getter
    private final String clientId;

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
        return true;
    }

    public boolean forwardDeltaToServer(Delta e) {
        return server.receive(new ClientDeltaEvent(clientId, e));
    }
}
