package com.catfisher.multiarielle.clientServer.event.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ClientChatEvent extends ClientEvent {
    String message;

    public ClientChatEvent(String clientId, String message) {
        super(clientId);
        this.message = message;
    }

    @Override
    public <Response> Response receive(ClientEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
