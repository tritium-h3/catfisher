package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Value;

@Value
public class Character {
    TextureRegion[][] appearance;
    String name;
}
