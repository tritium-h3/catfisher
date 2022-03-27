package com.catfisher.multiarielle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;


public class MultiArielle extends Game {
	@Getter
	private SpriteBatch batch;
	private Texture heroSpriteSheet;

	@Getter
	TextureRegion[][] heroSprites;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		heroSpriteSheet = new Texture("Hero 02 32.png");
		heroSprites = TextureRegion.split(heroSpriteSheet, 32, 32);

		this.setScreen(new LocalScreen(this));
	}



	@Override
	public void dispose () {
		batch.dispose();
		heroSpriteSheet.dispose();
	}
}
