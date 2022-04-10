package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum BackgroundTile {
    EMPTY(Sprite.EMPTY, false),
    GRASS(Sprite.GRASS, false),
    WATER(Sprite.WATER, true);

    Sprite appearance;
    boolean impassible;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
