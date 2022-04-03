package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEventVisitor;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.model.AbsoluteModel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.HashSet;

@Log4j2
public class ModelServer implements ClientEventVisitor<Boolean> {
    private final AbsoluteModel trueModel;
    private final Collection<ProxyClient> clients = new HashSet<>();

    public void removeClient(ChannelHandlerContext ctx) {
        clients.removeIf(client -> client.getCtx() == ctx);
    }

    public ModelServer() {
        trueModel = new AbsoluteModel();
    }

    public void addClient(ProxyClient client) {
        synchronized (this) {
            client.consume(new SynchronizeEvent(trueModel.getAllCharacters()));
            clients.add(client);
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
        if (trueModel.consume(e.getDelta())) {
            for (ProxyClient client : clients) {
                if ((!e.getClientId().equals(client.getClientId())) || (e.getDelta() instanceof CharacterAddDelta)) {
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
