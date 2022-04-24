package com.catfisher.multiarielle.clientServer.event.server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ServerChatEvent extends ServerEvent {
    String sender;
    String message;
    Instant timestamp;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
