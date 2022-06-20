package com.catfisher.multiarielle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.catfisher.multiarielle.coordinates.AbsoluteCoordinate;
import com.catfisher.multiarielle.coordinates.ScreenCoordinate;
import com.catfisher.multiarielle.coordinates.TileCoordinate;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.LocalModel;
import com.catfisher.multiarielle.sprite.Sprite;
import com.catfisher.multiarielle.sprite.SpriteAtlas;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Log4j2
public class LocalScreen implements Screen {

    private static Drawable singleColorDrawable(Color color) {
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    final MultiArielle game;
    final OrthographicCamera camera;
    final LocalModel localModel;
    final SpriteAtlas atlas;

    @Getter
    private Stage stage;
    private TextField messageArea;
    private ServerTextArea serverTextArea;
    private Table layoutTable;

    private ScreenCoordinate cursor = new ScreenCoordinate(0, 0, 0, 0);

    private static class ServerTextArea {
        private final Label[] labelBox;
        private final int rows;

        public ServerTextArea(Table layout, Label.LabelStyle style, int rows) {
            this.rows = rows;
            labelBox = new Label[rows];
            for (int row = rows - 1; row >= 0; row--) {
                labelBox[row] = new Label("*row " + row + "*", style);
                layout.add(labelBox[row]).growX().bottom();
                layout.row().expandX();
            }
        }

        public void addMessage(String message) {
            for(int row = rows - 1; row > 0; row--) {
                labelBox[row].setText(labelBox[row - 1].getText());
            }
            labelBox[0].setText(message);
        }
    }

    LocalScreen(MultiArielle game) {
        this.game = game;
        this.atlas = game.getAtlas();

        this.localModel = game.getLocalModel();

        stage = new Stage();

        layoutTable = new Table();
        layoutTable.setFillParent(true);
        layoutTable.pad(15f);
        stage.addActor(layoutTable);

        Skin skin = new Skin(Gdx.files.internal("cloud-skin/cloud-form-ui.json"));
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);

        tfs.background = singleColorDrawable(new Color(1f,1f, 1f, 0.2f));
        tfs.focusedBackground = singleColorDrawable(new Color(1f,1f, 1f, 0.5f));
        tfs.background.setLeftWidth(tfs.background.getLeftWidth() + 5);
        tfs.background.setRightWidth(tfs.background.getRightWidth() + 5);
        tfs.focusedBackground.setLeftWidth(tfs.focusedBackground.getLeftWidth() + 5);
        tfs.focusedBackground.setRightWidth(tfs.focusedBackground.getRightWidth() + 5);

        Label.LabelStyle ls = skin.get("default", Label.LabelStyle.class);
        ls.background = singleColorDrawable(new Color(1f,1f, 1f, 0.2f));
        ls.background.setLeftWidth(ls.background.getLeftWidth() + 5);
        ls.background.setRightWidth(ls.background.getRightWidth() + 5);

        layoutTable.row().expandX().expandY();
        serverTextArea = new ServerTextArea(layoutTable, ls, 3);
        messageArea = new TextField("Hello World", tfs);
        layoutTable.add(messageArea).growX().bottom();


        this.camera = new OrthographicCamera(640, 360);
    }

    public void setCursor(int x, int y) {
        synchronized (cursor) {
            cursor = new ScreenCoordinate(x / 2, y / 2,
                    (int) (camera.position.x - (camera.viewportWidth / 2)),
                    (int) (camera.position.y + (camera.viewportHeight / 2)));
        }
    }

    public MessageHolder getMessageHolder() {
        return new MessageHolder() {
            @Override
            public String readAndUnfocus() {
                stage.unfocus(messageArea);
                return messageArea.getText();
            }

            @Override
            public void clearMessage() {
                messageArea.setText("");
            }

            @Override
            public void focus() {
                stage.setKeyboardFocus(messageArea);
            }

            @Override
            public void receiveMessage(String message) {
                serverTextArea.addMessage(message);
                log.debug("Displaying text {}", message);
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

        AbstractModel.MutablePlacement mp = null;

        for (AbstractModel.MutablePlacement i :  localModel.getLocalModel().getAllCharacters()) {
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
                synchronized (cursor) {
                    cursor = new ScreenCoordinate(cursor.getX(), cursor.getY(),
                            (int) (camera.position.x - (camera.viewportWidth / 2)),
                            (int) (camera.position.y + (camera.viewportHeight / 2)));
                }
                camera.update();
            }
        }

        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.getBatch().begin();

        int range = 12;
        TileCoordinate playerCoords = new TileCoordinate(mp.getX(), mp.getY());
        int startX = playerCoords.getX() - range;
        int startY = playerCoords.getY() - range;
        int endX = playerCoords.getX() + range;
        int endY = playerCoords.getY() + range;
        List<Sprite>[][] placements = localModel.getSpritePlacements(startX, startY, endX, endY);
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                for (Sprite sprite : placements[x - startX][y - startY]) {
                    stage.getBatch().draw(atlas.getTextureRegion(sprite), x * 32, y * 32);
                }
            }
        }

        synchronized (cursor) {
            AbsoluteCoordinate displayCursorCoord = cursor.toTile().toAbsolute();
            if (displayCursorCoord.toTile().distanceTo(playerCoords) <= 3) {
                stage.getBatch().draw(atlas.getTextureRegion(Sprite.SELECTOR), displayCursorCoord.getX(), displayCursorCoord.getY());
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
