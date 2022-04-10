package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.delta.*;
import com.catfisher.multiarielle.sprite.Sprite;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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

    public void loadBackground(String filename) throws IOException {
        List<BackgroundTile[]> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                BackgroundTile[] tileLine =
                        Arrays.stream(values).map(
                                val -> BackgroundTile.values()[Integer.parseInt(val)]
                        ).toArray(BackgroundTile[]::new);
                lines.add(tileLine);
            }
        }
        BackgroundTile[][] loadedOrientation = lines.toArray(new BackgroundTile[0][0]);
        int loadedYSize = loadedOrientation[0].length;
        int loadedXSize = loadedOrientation.length;
        background = new BackgroundTile[loadedYSize][loadedXSize];
        for (int x = 0; x < loadedYSize; x++) {
            for (int y = 0; y < loadedXSize; y++) {
                background[x][y] = loadedOrientation[loadedXSize-y-1][x];
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
