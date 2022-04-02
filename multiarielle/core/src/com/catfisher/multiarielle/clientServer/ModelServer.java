package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.*;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.SynchronizeDelta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
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
            client.consume(
                    new ServerDeltaEvent(new SynchronizeDelta(trueModel.getAllCharacters()))
            );
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
        if (e.getDelta() instanceof SynchronizeDelta) {
            return false;
        }
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
