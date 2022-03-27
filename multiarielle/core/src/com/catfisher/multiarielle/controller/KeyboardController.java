package com.catfisher.multiarielle.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.catfisher.multiarielle.controller.event.Event;
import com.catfisher.multiarielle.controller.event.MoveEvent;
import com.catfisher.multiarielle.model.Character;

import java.util.HashMap;
import java.util.Map;

public class KeyboardController implements InputProcessor {
    private final Character hero;
    private final EventConsumer consumer;
    private final Map<Integer, Event> keyMap = new HashMap<>();

    public KeyboardController(Character hero, EventConsumer consumer) {
        this.hero = hero;
        this.consumer = consumer;
        keyMap.put(Input.Keys.UP, new MoveEvent(hero, 0, 1));
        keyMap.put(Input.Keys.DOWN, new MoveEvent(hero, 0, -1));
        keyMap.put(Input.Keys.RIGHT, new MoveEvent(hero, 1, 0));
        keyMap.put(Input.Keys.LEFT, new MoveEvent(hero, -1, 0));
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        Event event = keyMap.get(keycode);
        if (null == event) {
            return false;
        } else {
            consumer.consume(event);
            return true;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
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
