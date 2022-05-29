package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.sprite.Sprite;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor()
public abstract class SpriteEntity extends DrawableEntity {
    @Getter
    protected int x, y;

    @Getter
    private final Sprite appearance;

    @Getter
    private final String id;

    protected SpriteEntity(int x, int y, Sprite appearance) {
        this.x = x;
        this.y = y;
        this.appearance = appearance;
        this.id = UUID.randomUUID().toString();
    }
}
