package com.catfisher.multiarielle.model;

import com.badlogic.gdx.math.Vector2;
import com.catfisher.multiarielle.clientServer.event.server.SynchronizeEvent;
import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.delta.*;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

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
    private Map<Chunk.Address, Chunk> map = new HashMap<>();

    public void loadBackground(String filename, int x, int y) throws IOException, IndexOutOfBoundsException {
        map.put(Chunk.Address.ofAbsoluteCoords(x, y), Chunk.readFromFile(filename));
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
    public List<Sprite>[][] getSpritePlacements(int startX, int startY, int endX, int endY) {
        List<Sprite>[][] toReturn = new List[endX - startX][endY - startY];

        synchronized (this) {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    Chunk.Address chunkAddress = Chunk.Address.ofAbsoluteCoords(x, y);
                    Pair<Integer, Integer> chunkOffset = Chunk.Address.getOffset(x, y);
                    Chunk chunk = map.get(chunkAddress);
                    if  (chunk == null) {
                        toReturn[x-startX][y-startY] = new ArrayList<>();
                        toReturn[x-startX][y-startY].add(Sprite.EMPTY);
                    } else {
                        toReturn[x-startX][y-startY] = new ArrayList<>();
                        toReturn[x - startX][y - startY].add(map.get(chunkAddress).
                                getBgLayer()[chunkOffset.getLeft()][chunkOffset.getRight()].getAppearance());
                    }
                }
            }
            for (MutablePlacement mp : allCharacters) {
                if ((mp.getX() >= startX) &&
                        (mp.getX() < endX) &&
                        (mp.getY() >= startY) &&
                        (mp.getY() < endY)) {
                    toReturn[mp.getX()-startX][mp.getY()-startY].add(mp.getCharacter().getAppearance());
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
                    int newX = p.getX() + e.getDeltaX();
                    int newY = p.getY() + e.getDeltaY();

                    Chunk.Address newAddress = Chunk.Address.ofAbsoluteCoords(newX, newY);
                    if (map.get(newAddress) == null) {
                        return false;
                    }

                    BackgroundTile[][] bgLayer = map.get(newAddress).getBgLayer();

                    // TODO: Bug involving out of bounds
                    Pair<Integer, Integer> chunkOffset = Chunk.Address.getOffset(newX, newY);
                    if ((bgLayer[chunkOffset.getLeft()][chunkOffset.getRight()] == null) ||
                            (bgLayer[chunkOffset.getLeft()][chunkOffset.getRight()].isImpassible())) {
                        return false;
                    } else {
                        p.setX(newX);
                        p.setY(newY);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public Boolean visit(CharacterAddDelta e) {
        allCharacters.add(new MutablePlacement(e.getCharacter(), e.getX(), e.getY()));
        return true;
    }

    public Boolean synchronize(SynchronizeEvent e) {
        map = e.getMap();
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
