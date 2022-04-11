package com.catfisher.multiarielle;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.LocalModel;
import com.catfisher.multiarielle.model.Model;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class LocalScreen implements Screen {

    final MultiArielle game;
    final OrthographicCamera camera;
    final LocalModel localModel;
    final SpriteAtlas atlas;

    LocalScreen(MultiArielle game) {
        this.game = game;
        this.camera = new OrthographicCamera(640, 360);
        this.atlas = game.getAtlas();

        this.localModel = game.getLocalModel();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        AbsoluteModel.MutablePlacement mp = null;

        for (AbsoluteModel.MutablePlacement i :  localModel.getLocalModel().getAllCharacters()) {
            if (i.getCharacter().equals(game.getHero())) {
                mp = i;
            }
        }

        if (mp == null) {
            log.error("Didn't find character, character should exist");
        }
        else {
            // TODO: Get rid of magic numbers
            if (camera.position.x != mp.getX() * 32 || camera.position.y != mp.getY() * 32) {
                camera.position.x += ((mp.getX() * 32 - camera.position.x) / 4);
                camera.position.y += ((mp.getY() * 32 - camera.position.y) / 4);
                camera.update();
            }
        }

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();

        List<Sprite>[][] placements = localModel.getSpritePlacements(0, 0, 20, 20);
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                for (Sprite sprite : placements[x][y]) {
                    game.getBatch().draw(atlas.getTextureRegion(sprite), x * 32, y * 32);
                }
            }
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
