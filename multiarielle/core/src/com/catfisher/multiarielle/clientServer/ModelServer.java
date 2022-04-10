package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.ClientDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEvent;
import com.catfisher.multiarielle.clientServer.event.client.ClientEventVisitor;
import com.catfisher.multiarielle.clientServer.event.client.ConnectEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerConnectionAcknowledged;
import com.catfisher.multiarielle.clientServer.event.server.ServerDeltaEvent;
import com.catfisher.multiarielle.clientServer.event.server.ServerRejectDeltaEvent;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.delta.CharacterRemoveDelta;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.Character;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ModelServer implements ClientEventVisitor<Boolean> {
    @Getter
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
            SynchronizeEvent event;
            synchronized (trueModel) {
                event = new SynchronizeEvent(client.getSequenceNumberWatermark().get(), trueModel.getBackground(), trueModel.copyCharacters());
            }
            client.consume(event);
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

    Map<ProxyClient, SynchronizeEvent> generateSynchronizeEventForAllClients() {
        Map<ProxyClient, SynchronizeEvent> toReturn = new HashMap<>();
        synchronized(trueModel) {
            Collection<AbsoluteModel.MutablePlacement> placements = trueModel.copyCharacters();
            for (ProxyClient client : clients.values()) {
                SynchronizeEvent event = new SynchronizeEvent(client.getSequenceNumberWatermark().get(), trueModel.getBackground(), placements);
                toReturn.put(client, event);
            }
        }
        return toReturn;
    }

    public void synchronizeAllClients() {
        log.info("Synchronizing clients");
        Map<ProxyClient, SynchronizeEvent> events = generateSynchronizeEventForAllClients();
        for (Map.Entry<ProxyClient, SynchronizeEvent> event : events.entrySet()) {
            event.getKey().consume(event.getValue());
        }
    }

    @Override
    public Boolean visit(ClientDeltaEvent e) {
        Delta delta = e.getDelta();
        String senderId = e.getClientId();
        log.info("Consuming delta [{}] {}", senderId, delta);

        ProxyClient sender = clients.get(senderId);
            synchronized (trueModel) {
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
        }
        if (applyDelta(senderId, delta)) {
            return true;
        } else {
            sender.consume(new ServerRejectDeltaEvent(e.getSequenceNumber()));
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
