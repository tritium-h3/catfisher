package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.sprite.Sprite;
import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString(exclude = "appearance")
public class Character {
    Sprite appearance;
    String name;
}
