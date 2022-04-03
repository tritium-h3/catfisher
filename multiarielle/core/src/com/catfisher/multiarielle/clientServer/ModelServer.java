package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEventVisitor;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerConnectionAcknowledged;
import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.model.AbsoluteModel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ModelServer implements ClientEventVisitor<Boolean> {
    private final AbsoluteModel trueModel;
    private final Map<String, ProxyClient> clients = new HashMap<>();

    public void removeClient(ChannelHandlerContext ctx) {
        for (ProxyClient client : clients.values() ) {
            if (client.getCtx().equals(ctx)) {
                clients.remove(client.getClientId());
            }
        }
    }

    public ModelServer() {
        trueModel = new AbsoluteModel();
    }

    public void addClient(ProxyClient client) {
        synchronized (this) {
            log.info("All characters: {}", trueModel.getAllCharacters());
            clients.put(client.getClientId(), client);
            client.consume(new ServerConnectionAcknowledged());
            client.consume(new SynchronizeEvent(client.getSequenceNumberWatermark().get(), trueModel.getAllCharacters()));
        }
    }


    @Override
    public Boolean visit(ConnectEvent e) {
        ProxyClient client = new ProxyClient(e.getClientId(), e.getCtx());
        addClient(client);
        return true;
    }

    @Override
    public Boolean visit(ClientDeltaEvent e) {
        log.info("Consuming delta {}", e.getDelta());
        ProxyClient sender = clients.get(e.getClientId());
        sender.getSequenceNumberWatermark().getAndAccumulate(e.getSequenceNumber(), Long::max);
        if (trueModel.consume(e.getDelta())) {
            for (ProxyClient client : clients.values()) {
                if (!e.getClientId().equals(client.getClientId())) {
                    client.consume(new ServerDeltaEvent(e.getDelta()));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Boolean consume(ClientEvent e) {
        synchronized (this) {
            log.info("Server received event {}", e);
            return e.receive(this);
        }
    }


}
