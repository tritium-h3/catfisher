package com.catfisher.multiarielle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.catfisher.multiarielle.model.Model;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LocalScreen implements Screen {

    final MultiArielle game;
    final OrthographicCamera camera;
    final Model localModel;
    final SpriteAtlas atlas;

    LocalScreen(MultiArielle game) {
        this.game = game;
        this.camera = new OrthographicCamera(640, 640);
        this.atlas = game.getAtlas();

        this.localModel = game.getLocalModel();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        game.getBatch().begin();

        for (Model.SpritePlacement placement : localModel.getSpritePlacements(0, 0, 20, 20)) {
            game.getBatch().draw(atlas.getTextureRegion(placement.getSprite()), placement.getX() * 32, placement.getY() * 32);
        }

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
