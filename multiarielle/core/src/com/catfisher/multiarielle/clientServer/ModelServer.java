package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.clientServer.event.client.*;
import com.catfisher.multiarielle.clientServer.event.server.*;
import com.catfisher.multiarielle.controller.delta.CharacterAddDelta;
import com.catfisher.multiarielle.controller.delta.CharacterRemoveDelta;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.coordinates.TileCoordinate;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.Character;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.model.ServerModel;
import com.catfisher.multiarielle.worldgen.WorldGenerator;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ModelServer implements ClientEventVisitor<Boolean> {
    public static final String SERVER_ID = "SERVER";

    String password;
    @Getter
    private final ServerModel trueModel;
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
                clientCharacters.remove(client.getClientId());
            }
        }
    }

    public ModelServer(String password, WorldGenerator generator) {
        this.password = password;
        trueModel = new ServerModel(generator);
    }

    public void addClient(ProxyClient client) {
        synchronized (this) {
            log.info("All characters: {}", trueModel.getAllCharacters());
            clients.put(client.getClientId(), client);
            client.consume(new ServerConnectionAcknowledged());
            SynchronizeEvent event;
            synchronized (trueModel) {
                event = generateSynchronizeEventForClient(client);
            }
            client.consume(event);
        }
    }


    @Override
    public Boolean visit(ConnectEvent e) {
        log.info("Logging in client {}", e.getClientId());
        ProxyClient client = new ProxyClient(e.getClientId(), e.getCtx());
        if (clients.containsKey(e.getClientId())) {
            client.consume(new ServerConnectionRejected("User with same name logged in"));
            return true;
        }
        else if (!e.getPassword().equals(password)) {
            client.consume(new ServerConnectionRejected("Password is incorrect"));
            return true;
        }

        addClient(client);
        return true;
    }

    public boolean applyDelta(Delta delta) {
        return applyDelta(SERVER_ID, delta);
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

    private Chunk.Address getLocationOfClient(ProxyClient client) {
        Character clientCharacter = clientCharacters.get(client.getClientId());
        if (clientCharacter != null) {
            for (AbstractModel.MutablePlacement placement : trueModel.getAllCharacters()) {
                if (placement.getCharacter().getName().equals(clientCharacter.getName())) {
                    return Chunk.Address.ofTileCoords(new TileCoordinate(placement.getX(), placement.getY()));
                }
            }
        }
        return Chunk.Address.ofTileCoords(new TileCoordinate(0, 0));
    }

    private SynchronizeEvent generateSynchronizeEventForClient(ProxyClient client) {
        return new SynchronizeEvent(client.getSequenceNumberWatermark().get(),
                trueModel.getSubMapAround(getLocationOfClient(client)),
                trueModel.copyCharacters());
    }

    private Map<ProxyClient, SynchronizeEvent> generateSynchronizeEventForAllClients() {
        Map<ProxyClient, SynchronizeEvent> toReturn = new HashMap<>();
        synchronized(trueModel) {
            for (ProxyClient client : clients.values()) {
                toReturn.put(client, generateSynchronizeEventForClient(client));
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

    @Override
    public Boolean visit(ClientChatEvent e) {
        Instant chatTimeStamp = Instant.now();
        for (ProxyClient client : clients.values()) {
            client.consume(new ServerChatEvent(e.getClientId(), e.getMessage(), chatTimeStamp));
        }
        return true;
    }

    public Boolean consume(ClientEvent e) {
        synchronized (this) {
            log.info("Server received event {}", e);
            return e.receive(this);
        }
    }

}
