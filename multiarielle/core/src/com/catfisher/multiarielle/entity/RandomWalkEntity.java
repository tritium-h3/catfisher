package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.coordinates.TileCoordinate;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
public class RandomWalkEntity extends MovingEntity {
    private static Random seededRandom = new Random();

    @JsonCreator
    public RandomWalkEntity(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("appearance") Sprite appearance,
            @JsonProperty("id") String id) {
        super(x, y, appearance, id);
    }

    public RandomWalkEntity(int x, int y, Sprite appearance) {
        super(x, y, appearance);
    }

    @Override
    public EntityChangeDelta update(AbstractModel abstractModel) {
        try {
            int xDiff = seededRandom.nextInt(3) - 1;
            int newX = x + xDiff;
            int yDiff = seededRandom.nextInt(3) - 1;
            int newY = y + yDiff;
            return new EntityChangeDelta(
                    getId(),
                    Chunk.Address.ofTileCoords(new TileCoordinate(x, y)),
                    objectMapper.writeValueAsString(new WalkDelta(newX, newY))
            );
        } catch (JsonProcessingException exn) {
            log.error(exn);
            throw new RuntimeException(exn);
        }
    }
}
