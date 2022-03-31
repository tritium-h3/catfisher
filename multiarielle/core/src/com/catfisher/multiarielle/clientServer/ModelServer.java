package com.catfisher.multiarielle.clientServer;

import com.catfisher.multiarielle.controller.EventConsumer;
import com.catfisher.multiarielle.controller.event.CharacterAddEvent;
import com.catfisher.multiarielle.controller.event.Event;
import com.catfisher.multiarielle.controller.event.SynchronizeEvent;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Log4j2
public class ModelServer {
    private final AbsoluteModel trueModel;
    private final Collection<ProxyClient> clients = new HashSet<>();

    public void removeClient(ChannelHandlerContext ctx) {
        clients.removeIf(client -> client.getCtx() == ctx);
    }


    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class ClientEvent {
        @JsonProperty("clientId")
        String clientId;
        @JsonProperty("event")
        Event event;
    }

    public ModelServer() {
        trueModel = new AbsoluteModel();
    }

    public void addClient(ProxyClient client) {
        synchronized (this) {
            client.consume(
                    new SynchronizeEvent(trueModel.getAllCharacters())
            );
            clients.add(client);
        }
    }

    public Boolean receive(ClientEvent e) {
        synchronized (this) {
            log.info("Server received event {}", e);
            if (e.getEvent() instanceof SynchronizeEvent) {
                return false;
            }
            if (trueModel.consume(e.getEvent())) {
                for (ProxyClient client : clients) {
                    if ((!e.getClientId().equals(client.getClientId())) || e.getEvent() instanceof CharacterAddEvent) {
                        client.consume(e.getEvent());
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
