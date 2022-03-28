package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.sprite.Sprite;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "appearance")
public class Character {
    Sprite appearance;
    String name;
}
