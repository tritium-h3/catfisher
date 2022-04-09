package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.delta.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class AbsoluteModel implements Model, DeltaVisitor<Boolean>, DeltaConsumer<Boolean> {

    @Getter
    private Collection<MutablePlacement> allCharacters = new HashSet<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    public static class MutablePlacement {
        Character character;
        int x;
        int y;

        public MutablePlacement copy() {
            return new MutablePlacement(character, x, y);
        }
    }

    public Collection<MutablePlacement> copyCharacters() {
        return allCharacters.stream().map(MutablePlacement::copy).collect(Collectors.toList());
    }

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
    public Boolean visit(MoveDelta e) {
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
    public Boolean visit(CharacterAddDelta e) {
        allCharacters.add(new MutablePlacement(e.getCharacter(), e.getX(), e.getY()));
        return true;
    }

    public Boolean synchronize(SynchronizeEvent e) {
        allCharacters = e.getAllCharacters();
        return true;
    }

    @Override
    public Boolean visit(CharacterRemoveDelta characterRemoveEvent) {
        allCharacters.removeIf(place -> place.getCharacter().getName().equals(characterRemoveEvent.getCharacter().getName()));
        return true;
    }

    @Override
    public Boolean consume(Delta e) {
        return e.accept(this);
    }
}
