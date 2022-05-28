package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class RandomWalkEntity extends SpriteEntity {
    static Random seededRandom = new Random();

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
        int xDiff = seededRandom.nextInt(3) - 1;
        int newX = getX() + xDiff;
        int yDiff = seededRandom.nextInt(3) - 1;
        int newY = getY() + yDiff;
        return new EntityChangeDelta(Chunk.Address.ofAbsoluteCoords(getX(), getY()), getId(),
                Chunk.Address.ofAbsoluteCoords(newX, newY), new RandomWalkEntity(newX, newY, getAppearance(), getId()));
    }
}
