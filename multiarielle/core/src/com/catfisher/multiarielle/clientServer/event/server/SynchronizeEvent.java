package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.model.AbsoluteModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SynchronizeEvent extends ServerEvent {
    long sequenceNumberWatermark;

    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> v) {
        return v.visit(this);
    }
}