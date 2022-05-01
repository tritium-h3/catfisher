package com.catfisher.multiarielle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MainMenuScreen implements Screen {
    private MultiArielle game;
    private Stage stage;
    private TextField username;
    private TextField password;
    private TextField address;
    private TextField port;
    private Button button;

    MainMenuScreen(MultiArielle game, String error, MultiArielle.ConnectionConfiguration defaultConfiguration) {
        this.game = game;

        TextureAtlas atlas = new TextureAtlas("cloud-skin/cloud-form-ui.atlas");
        Skin skin = new Skin(Gdx.files.internal("cloud-skin/cloud-form-ui.json"), atlas);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();

        Label.LabelStyle ls = skin.get("default", Label.LabelStyle.class);
        TextButton.TextButtonStyle tbs = skin.get("default", TextButton.TextButtonStyle.class);
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);

        Label label = new Label("Username", ls);
        username = new TextField(defaultConfiguration.getUsername(), tfs);
        mainTable.add(label);
        mainTable.add(username);
        mainTable.row();

        Label passLabel = new Label("Password", ls);
        password = new TextField(defaultConfiguration.getPassword(), tfs);
        mainTable.add(passLabel);
        mainTable.add(password);
        mainTable.row();

        Label addressLabel = new Label("Address", ls);
        address = new TextField(defaultConfiguration.getAddress(), tfs);
        mainTable.add(addressLabel);
        mainTable.add(address);
        mainTable.row();

        Label portLabel = new Label("Port", ls);
        port = new TextField(defaultConfiguration.getPort() == -1 ? "" : Integer.toString(defaultConfiguration.getPort()), tfs);
        mainTable.add(portLabel);
        mainTable.add(port);
        mainTable.row();

        button = new TextButton("Connect to server", tbs);
        mainTable.add(button);
        mainTable.row();

        Label errorLabel = new Label(error, ls);
        mainTable.add(errorLabel);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.runGame(new MultiArielle.ConnectionConfiguration(username.getText(), password.getText(), address.getText(), Integer.parseInt(port.getText())));
            }
        });
        stage.addActor(mainTable);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
        stage.draw();
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
