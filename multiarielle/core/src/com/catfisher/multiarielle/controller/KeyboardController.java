package com.catfisher.multiarielle.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.catfisher.multiarielle.ChatHandler;
import com.catfisher.multiarielle.controller.delta.Delta;
import com.catfisher.multiarielle.controller.delta.MoveDelta;
import com.catfisher.multiarielle.model.Character;

import java.util.HashMap;
import java.util.Map;

public class KeyboardController implements InputProcessor {
    private final Character hero;
    private final DeltaConsumer consumer;
    private final ChatHandler chatHandler;
    private final Map<Integer, Delta> keyMap = new HashMap<>();

    public KeyboardController(Character hero, DeltaConsumer consumer, ChatHandler chatHandler) {
        this.hero = hero;
        this.consumer = consumer;
        this.chatHandler = chatHandler;
        keyMap.put(Input.Keys.UP, new MoveDelta(hero, 0, 1));
        keyMap.put(Input.Keys.DOWN, new MoveDelta(hero, 0, -1));
        keyMap.put(Input.Keys.RIGHT, new MoveDelta(hero, 1, 0));
        keyMap.put(Input.Keys.LEFT, new MoveDelta(hero, -1, 0));
    }

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
        Delta delta = keyMap.get(keycode);
        if (null == delta) {
            return true;
        } else {
            consumer.consume(delta);
            return true;
        }
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
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
