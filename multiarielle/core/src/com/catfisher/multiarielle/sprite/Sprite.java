package com.catfisher.multiarielle.sprite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Sprite {
    EMPTY(Spritesheet.OUTDOOR_SPRITESHEET_NAME,1, 0),

    GRASS(Spritesheet.OUTDOOR_SPRITESHEET_NAME,0, 0),
    WATER(Spritesheet.OUTDOOR_SPRITESHEET_NAME, 6, 16),

    HERO(Spritesheet.HERO_SPRITESHEET_NAME, 0, 0),
    TOUCAN(Spritesheet.TOUCAN_SPRITESHEET_NAME, 0, 0),
    VILLAGER(Spritesheet.VILLAGER_SPRITESHEET_NAME, 0, 0),
    VILLAIN(Spritesheet.VILLAIN_SPRITESHEET_NAME, 0, 0),

    WALL(Spritesheet.INDOOR_SPRITESHEET_NAME, 2, 5);

    private String sheetName;
    private int sheetX;
    private int sheetY;
}
