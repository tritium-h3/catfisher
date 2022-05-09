package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class StaticSprite extends SpriteEntity {
    @JsonCreator
    public StaticSprite(@JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("appearance") Sprite appearance) {
        super(x, y, appearance);
    }

    @Override
    public void update(AbstractModel abstractModel) {

    }
}
