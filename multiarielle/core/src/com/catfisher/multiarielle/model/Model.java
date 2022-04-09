package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.catfisher.multiarielle.sprite.Sprite;
import lombok.Value;

public interface Model {

    Sprite[][] getSpritePlacements(int startX, int startY, int endX, int endY);
}
