package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class StaticSprite extends SpriteEntity {
    @JsonCreator
    public StaticSprite(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("appearance") Sprite appearance,
            @JsonProperty("id") String id
            ) {
        super(x, y, appearance, id);
    }

    @Override
    public EntityChangeDelta update(AbstractModel abstractModel) {
        return null;
    }

    @Override
    public void acceptUpdate(String update, AbstractModel abstractModel) {

    }
}
