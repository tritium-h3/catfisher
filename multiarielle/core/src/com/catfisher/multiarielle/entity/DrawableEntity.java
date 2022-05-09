package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.sprite.Sprite;

public abstract class DrawableEntity implements Entity {
    public abstract Sprite getAppearance();
    public abstract int getX();
    public abstract int getY();
}
