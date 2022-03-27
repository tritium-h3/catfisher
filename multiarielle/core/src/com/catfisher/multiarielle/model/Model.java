package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Value;

public interface Model {

    @Value
    class SpritePlacement {
        int x, y;
        TextureRegion sprite;
    }

    Iterable<SpritePlacement> getSpritePlacements(int startX, int startY, int endX, int endY);
}
