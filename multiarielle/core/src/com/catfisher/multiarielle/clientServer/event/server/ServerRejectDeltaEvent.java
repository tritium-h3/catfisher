package com.catfisher.multiarielle.clientServer.event.server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ServerRejectDeltaEvent extends ServerEvent {
    long sequenceId;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
