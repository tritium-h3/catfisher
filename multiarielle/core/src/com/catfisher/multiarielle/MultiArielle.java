package com.catfisher.multiarielle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.catfisher.multiarielle.clientServer.ModelClient;
import com.catfisher.multiarielle.clientServer.ModelServer;
import com.catfisher.multiarielle.controller.event.CharacterAddEvent;
import com.catfisher.multiarielle.controller.KeyboardController;
import com.catfisher.multiarielle.model.Character;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.LocalModel;
import lombok.Getter;


public class MultiArielle extends Game {
	@Getter
	private SpriteBatch batch;
	private Texture heroSpriteSheet;

	private ModelServer server;
	private ModelClient client;

	@Getter
	private LocalModel localModel;

	@Getter
	private KeyboardController keyboardController;

	@Getter
	TextureRegion[][] heroSprites;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		heroSpriteSheet = new Texture("Hero 02 32.png");
		heroSprites = TextureRegion.split(heroSpriteSheet, 32, 32);

		Character hero = new Character(heroSprites, "hero");

		localModel = new LocalModel();
		server = new ModelServer();
		client = new ModelClient(localModel.getLocalModel());
		localModel.associateClient(client);
		client.associateServer(server);

		localModel.consume(new CharacterAddEvent(hero, 10, 10));
		keyboardController = new KeyboardController(hero, localModel);

		Gdx.input.setInputProcessor(keyboardController);

		this.setScreen(new LocalScreen(this));
	}



	@Override
	public void dispose () {
		batch.dispose();
		heroSpriteSheet.dispose();
	}
}
