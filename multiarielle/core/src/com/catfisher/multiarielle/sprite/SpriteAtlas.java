package com.catfisher.multiarielle.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpriteAtlas {
    private final Map<Sprite, TextureRegion[][]> spriteSheetMap = new HashMap<>();

    public SpriteAtlas() {
        Texture heroSpriteSheet = new Texture("Hero 02 32.png");
        Texture outdoorSpriteSheet = new Texture("Outdoor.png");
        TextureRegion[][] outdoorTileset = TextureRegion.split(outdoorSpriteSheet, 32, 32);
        TextureRegion[][] emptyTileset = {{outdoorTileset[0][0]}};
        spriteSheetMap.put(Sprite.HERO, TextureRegion.split(heroSpriteSheet, 32, 32));
        spriteSheetMap.put(Sprite.EMPTY,  emptyTileset);
    }

    public TextureRegion getTextureRegion(Sprite sprite) {
        return spriteSheetMap.get(sprite)[0][0];
    }

    public void dispose() {
        for (Sprite sprite : Sprite.values()) {
            spriteSheetMap.get(sprite)[0][0].getTexture().dispose();
        }
    }

}
