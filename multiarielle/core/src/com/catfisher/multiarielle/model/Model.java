package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.catfisher.multiarielle.sprite.Sprite;
import lombok.Value;

import java.util.List;

public interface Model {

    void update();

    List<Sprite>[][] getSpritePlacements(int startX, int startY, int endX, int endY);
}
