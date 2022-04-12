package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.Chunk;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SynchronizeEvent extends ServerEvent {
    long sequenceNumberWatermark;

    Map<Integer, Map<Integer, Chunk>> map;
    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> v) {
        return v.visit(this);
    }
}
