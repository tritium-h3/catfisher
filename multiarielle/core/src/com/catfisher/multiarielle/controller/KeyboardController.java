package com.catfisher.multiarielle.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.catfisher.multiarielle.ChatHandler;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.CharacterMoveDelta;
import com.catfisher.multiarielle.model.Character;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class KeyboardController implements InputProcessor {
    private final Character hero;
    private final UiController uiController;
    private final ChatHandler chatHandler;

    @Override
    public boolean keyUp(int keycode) {
        if (!chatHandler.isInChat()) {
            if (keycode == Input.Keys.T) {
                chatHandler.startChatting();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (chatHandler.isInChat()) {
            if (keycode == Input.Keys.ENTER) {
                chatHandler.acceptChatMessage();
                return true;
            }
            return false;
        }
        switch (keycode) {
            case Input.Keys.UP: return uiController.moveCharacter(hero, 0, 1);
            case Input.Keys.DOWN: return uiController.moveCharacter(hero, 0, -1);
            case Input.Keys.RIGHT: return uiController.moveCharacter(hero, 1, 0);
            case Input.Keys.LEFT: return uiController.moveCharacter(hero, -1, 0);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return !chatHandler.isInChat();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return uiController.addWall();
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        uiController.moveCursor(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
