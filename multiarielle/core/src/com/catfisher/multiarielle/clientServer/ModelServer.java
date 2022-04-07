package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEventVisitor;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerConnectionAcknowledged;
import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.delta.CharacterRemoveDelta;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.Character;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ModelServer implements ClientEventVisitor<Boolean> {
    private final AbsoluteModel trueModel;
    private final Map<String, ProxyClient> clients = new HashMap<>();
    private final Map<String, Character> clientCharacters = new HashMap<>();

    public void removeClient(ChannelHandlerContext ctx) {
        for (ProxyClient client : clients.values() ) {
            if (client.getCtx().equals(ctx)) {
                clients.remove(client.getClientId());
                Character toRemove = clientCharacters.get(client.getClientId());
                if (toRemove != null) {
                    applyDelta(client.getClientId(), new CharacterRemoveDelta(toRemove));
                }
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

    private boolean applyDelta(String senderId, Delta delta) {
        if (trueModel.consume(delta)) {
            for (ProxyClient client : clients.values()) {
                if (!senderId.equals(client.getClientId())) {
                    client.consume(new ServerDeltaEvent(delta));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void synchronizeAllClients() {
        log.info("Synchronizing clients");
        for (ProxyClient client : clients.values()) {
            client.consume(new SynchronizeEvent(client.getSequenceNumberWatermark().get(), trueModel.getAllCharacters()));
        }
    }

    @Override
    public Boolean visit(ClientDeltaEvent e) {
        Delta delta = e.getDelta();
        String senderId = e.getClientId();
        log.info("Consuming delta [{}] {}", senderId, delta);

        ProxyClient sender = clients.get(senderId);
        sender.getSequenceNumberWatermark().getAndAccumulate(e.getSequenceNumber(), Long::max);
        if (delta instanceof CharacterAddDelta) {
            CharacterAddDelta characterAddDelta = (CharacterAddDelta) delta;
            if (clientCharacters.containsKey(senderId)) {
                log.error("Client already has a character {}", senderId);
                return false;
            } else {
                clientCharacters.put(senderId, characterAddDelta.getCharacter());
            }
        }
        applyDelta(senderId, delta);
        return true;
    }

    public Boolean consume(ClientEvent e) {
        synchronized (this) {
            log.info("Server received event {}", e);
            return e.receive(this);
        }
    }

}
