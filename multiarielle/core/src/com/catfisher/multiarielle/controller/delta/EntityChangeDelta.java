package com.catfisher.multiarielle.controller.delta;

import com.catfisher.multiarielle.controller.DeltaVisitor;
import com.catfisher.multiarielle.entity.Entity;
import com.catfisher.multiarielle.model.Chunk;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EntityChangeDelta extends Delta {
    Chunk.Address oldChunk;
    String oldEntity;
    Chunk.Address newChunk;
    Entity newEntity;

    @Override
    public <Response> Response accept(DeltaVisitor<Response> v) {
        return v.visit(this);
    }

    @Override
    public Delta invert() {
        return null;
    }
}
