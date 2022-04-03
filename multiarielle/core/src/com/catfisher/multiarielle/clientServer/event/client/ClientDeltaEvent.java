package com.catfisher.multiarielle.clientServer.event.client;

import com.catfisher.multiarielle.controller.delta.Delta;
import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class ClientDeltaEvent extends ClientEvent {
    Delta delta;
    long sequenceNumber;

    public ClientDeltaEvent(String id, long sequenceNumber, Delta delta) {
        super(id);
        this.delta = delta;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public <Response> Response receive(ClientEventVisitor<Response> visitor) {
        return visitor.visit(this);
    }
}
