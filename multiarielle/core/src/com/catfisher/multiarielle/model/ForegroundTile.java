package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ForegroundTile {
    EMPTY(Sprite.EMPTY, false),
    WALL(Sprite.WALL, true);

    Sprite appearance;
    boolean impassible;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
