package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.delta.*;
import com.catfisher.multiarielle.sprite.Sprite;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
public class AbsoluteModel implements Model, DeltaVisitor<Boolean>, DeltaConsumer<Boolean> {

    @Getter
    private Collection<MutablePlacement> allCharacters = new HashSet<>();

    @Getter
    private BackgroundTile[][] background = new BackgroundTile[20][20];
    {
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                background[x][y] = BackgroundTile.EMPTY;
            }
        }
    }

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
    public Sprite[][] getSpritePlacements(int startX, int startY, int endX, int endY) {
        Sprite[][] toReturn = new Sprite[endX - startX][endY - startY];
        synchronized (this) {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    if (background[x][y] == null) {
                        toReturn[x-startX][y-startY] = Sprite.EMPTY;
                    } else {
                        toReturn[x - startX][y - startY] = background[x][y].getAppearance();
                    }
                }
            }
            for (MutablePlacement mp : allCharacters) {
                if ((mp.getX() >= startX) &&
                        (mp.getX() < endX) &&
                        (mp.getY() >= startY) &&
                        (mp.getY() < endY)) {
                    toReturn[mp.getX()-startX][mp.getY()-startY] = mp.getCharacter().getAppearance();
                }
            }
        }
        return toReturn;
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
        background = e.getBackground();
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
