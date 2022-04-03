package com.catfisher.multiarielle.clientServer.event.client;

import com.catfisher.multiarielle.controller.delta.Delta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ClientDeltaEvent extends ClientEvent {
    Delta delta;

    public ClientDeltaEvent(String id, Delta delta) {
        super(id);
        this.delta = delta;
    }

    @Override
    public <Response> Response receive(ClientEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
