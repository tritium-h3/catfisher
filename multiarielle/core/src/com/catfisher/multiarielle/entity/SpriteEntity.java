package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.sprite.Sprite;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
public abstract class SpriteEntity extends DrawableEntity {
    @Getter
    private int x, y;

    @Getter
    private final Sprite appearance;

}
