package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.event.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AbsoluteModel implements Model, EventVisitor<Boolean>, EventConsumer<Boolean> {
    @Data
    @AllArgsConstructor
    public static class MutablePlacement {
        Character character;
        int x;
        int y;
    }

    @Getter
    private Collection<MutablePlacement> allCharacters = new HashSet<>();

    @Override
    public Iterable<SpritePlacement> getSpritePlacements(int startX, int startY, int endX, int endY) {
        synchronized (this) {
            return allCharacters.stream().filter(
                    mp -> ((mp.getX() >= startX) &&
                            (mp.getX() < endX) &&
                            (mp.getY() >= startY) &&
                            (mp.getY() < endY)))
                    .map(mp -> new SpritePlacement(mp.getX(), mp.getY(), mp.getCharacter().getAppearance()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Boolean visit(MoveEvent e) {
        synchronized (this) {
            for (MutablePlacement p : allCharacters) {
                if (p.getCharacter().getName().equals(e.getCharacter().getName())) {
                    p.setX(p.getX() + e.getDeltaX());
                    p.setY(p.getY() + e.getDeltaY());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean visit(CharacterAddEvent e) {
        allCharacters.add(new MutablePlacement(e.getCharacter(), e.getX(), e.getY()));
        return true;
    }

    @Override
    public Boolean visit(SynchronizeEvent e) {
        allCharacters = new HashSet<>(e.getAllCharacters().size());
        for (MutablePlacement mp : e.getAllCharacters()) {
            allCharacters.add(new MutablePlacement(mp.getCharacter(), mp.getX(), mp.getY()));
        }
        return true;
    }

    @Override
    public Boolean visit(ConnectEvent connectEvent) {
        return false;
    }

    @Override
    public Boolean consume(Event e) {
        return e.accept(this);
    }
}
