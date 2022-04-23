package com.catfisher.multiarielle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.catfisher.multiarielle.model.AbsoluteModel;
import com.catfisher.multiarielle.model.LocalModel;
import com.catfisher.multiarielle.model.Model;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class LocalScreen implements Screen {

    private static Drawable singleColorDrawable(Color color) {
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    public final static Drawable BACKGROUND = singleColorDrawable(new Color(0.0f, 0.0f, 0.0f, 0.50f));

    final MultiArielle game;
    final OrthographicCamera camera;
    final LocalModel localModel;
    final SpriteAtlas atlas;

    @Getter
    private Stage stage;
    private TextField messageArea;
    private Table layoutTable;

    boolean inChat = false;

    LocalScreen(MultiArielle game) {
        this.game = game;
        this.atlas = game.getAtlas();

        this.localModel = game.getLocalModel();

        stage = new Stage();
        layoutTable = new Table();
        layoutTable.setFillParent(true);
        stage.addActor(layoutTable);

        messageArea = new TextField("Hello World", new TextField.TextFieldStyle(game.getCharterBody(), Color.WHITE, null, null, BACKGROUND));
        layoutTable.add(messageArea).expandY().growX().bottom();

        stage.setKeyboardFocus(messageArea);

        this.camera = new OrthographicCamera(640, 360);
    }

    public MessageHolder getMessageHolder() {
        return new MessageHolder() {
            @Override
            public String getCurrentMessage() {
                return messageArea.getText();
            }

            @Override
            public void clearMessage() {
                messageArea.setText("");
            }
        };
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        stage.draw();

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

        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.getBatch().begin();

        int range = 12;
        int playerX = mp.getX();
        int playerY = mp.getY();
        int startX = playerX - range;
        int startY = playerY - range;
        int endX = playerX + range;
        int endY = playerY + range;
        List<Sprite>[][] placements = localModel.getSpritePlacements(startX, startY, endX, endY);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (Sprite sprite : placements[x - startX][y - startY]) {
                    stage.getBatch().draw(atlas.getTextureRegion(sprite), x * 32, y * 32);
                }
            }
        }

        stage.getBatch().end();

        stage.draw();

    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

}
