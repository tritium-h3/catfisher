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
    private Map<Integer, Map<Integer, Chunk>> map = new HashMap<>(new HashMap<>());

    public void loadBackground(String filename, int x, int y) throws IOException, IndexOutOfBoundsException {
        if (map.get(y) == null) {
            map.put(y, new HashMap<>());
        }

        map.get(y).put(x, Chunk.readFromFile(filename));
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
            	Map<Integer, Chunk> mapRow = map.get((int) Math.floor(x / 20));
            	boolean isNullRow = (mapRow == null);
                for (int y = startY; y < endY; y++) {
                    if  (isNullRow) {
                    	toReturn[x-startX][y-startY] = new ArrayList<>();
                    	toReturn[x-startX][y-startY].add(Sprite.EMPTY);
                    } else {
                    	Chunk chunk = mapRow.get((int) Math.floor(x / 20));
                    	if  (chunk == null) {
                    		toReturn[x-startX][y-startY] = new ArrayList<>();
                    		toReturn[x-startX][y-startY].add(Sprite.EMPTY);
                    	}
                    	else {
                    		toReturn[x-startX][y-startY] = new ArrayList<>();
                    		// TODO: One liner bad, make better
                    		toReturn[x - startX][y - startY].add(map.get((int) Math.floor(x / 20)).get((int) Math.floor(y / 20)).getBgLayer()[Math.floorMod(x, 20)][Math.floorMod(y, 20)].getAppearance());
                    	}
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

                    if (map.get(newX / 20) == null || map.get(newX / 20).get(newY / 20) == null) {
                        return false;
                    }
                   
          
                    BackgroundTile[][] bgLayer = map.get(newX / 20).get(newY / 20).getBgLayer();

                    // TODO: Bug involving out of bounds
                    if ((bgLayer[Math.floorMod(newX, 20)][Math.floorMod(newY, 20)] == null) ||(bgLayer[Math.floorMod(newX, 20)][Math.floorMod(newY, 20)].isImpassible())) {
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
