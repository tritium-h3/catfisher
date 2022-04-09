package com.catfisher.multiarielle.sprite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Sprite {
    EMPTY(Spritesheet.OUTDOOR_SPRITESHEET_NAME,0, 0),
    HERO(Spritesheet.HERO_SPRITESHEET_NAME, 0, 0);

    private String sheetName;
    private int sheetX;
    private int sheetY;
}
