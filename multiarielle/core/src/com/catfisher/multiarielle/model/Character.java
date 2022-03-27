package com.catfisher.multiarielle.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "appearance")
public class Character {
    TextureRegion[][] appearance;
    String name;
}
