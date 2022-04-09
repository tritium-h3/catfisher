package com.catfisher.multiarielle.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LazyMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class SpriteAtlas {
    private final Map<String, TextureRegion[][]> spriteSheetMap =
            LazyMap.lazyMap(new HashMap<>(), sheetName -> TextureRegion.split(new Texture(sheetName), 32, 32));

    public TextureRegion getTextureRegion(Sprite sprite) {
        return spriteSheetMap.get(sprite.getSheetName())[sprite.getSheetX()][sprite.getSheetY()];
    }

    public void dispose() {
        for (Sprite sprite : Sprite.values()) {
            spriteSheetMap.get(sprite)[0][0].getTexture().dispose();
        }
    }

}
