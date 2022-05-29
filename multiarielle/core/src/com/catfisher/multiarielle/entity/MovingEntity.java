package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
public abstract class MovingEntity extends SpriteEntity {
    protected static ObjectMapper objectMapper = new ObjectMapper();

    @Value
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    protected static class WalkDelta {
        int newX, newY;
    }

    protected MovingEntity(int x, int y, Sprite appearance) {
        super(x, y, appearance);
    }

    protected MovingEntity(int x, int y, Sprite appearance, String id) {
        super(x, y, appearance, id);
    }

    @Override
    public void acceptUpdate(String update, AbstractModel abstractModel) {
        try {
            WalkDelta delta = objectMapper.readValue(update, WalkDelta.class);
            Chunk.Address oldChunk = Chunk.Address.ofAbsoluteCoords(x, y);
            x = delta.getNewX();
            y = delta.getNewY();
            Chunk.Address newChunk = Chunk.Address.ofAbsoluteCoords(x, y);
            if (!oldChunk.equals(newChunk)) {
                abstractModel.moveEntityToChunk(this, oldChunk, newChunk);
            }
        } catch (JsonProcessingException exn) {
            log.error(exn);
            throw new RuntimeException(exn);
        }
    }
}
