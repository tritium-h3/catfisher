package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class LocalModel implements Model {
    private final TextureRegion[][] heroSprite;

    @Override
    public Iterable<SpritePlacement> getSpritePlacements(int startX, int startY, int endX, int endY) {
        return Arrays.asList(new SpritePlacement(10, 10, heroSprite[0][0]));
    }
}
