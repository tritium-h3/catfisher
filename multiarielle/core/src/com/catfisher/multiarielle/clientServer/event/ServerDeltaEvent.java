package com.catfisher.multiarielle.clientServer.event;

import com.catfisher.multiarielle.controller.delta.Delta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ServerDeltaEvent extends ServerEvent {
    Delta delta;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
