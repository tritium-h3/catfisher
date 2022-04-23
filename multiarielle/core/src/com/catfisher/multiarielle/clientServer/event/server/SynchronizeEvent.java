package com.catfisher.multiarielle.clientServer.event.server;

import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.Chunk;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

    @JsonSerialize(keyUsing = Chunk.Address.KeySer.class)
    @JsonDeserialize(keyUsing = Chunk.Address.KeyDeser.class)
    Map<Chunk.Address, Chunk> map;

    Collection<AbsoluteModel.MutablePlacement> allCharacters;

    @Override
    public <Response> Response receive(ServerEventVisitor<Response> v) {
        return v.visit(this);
    }
}
