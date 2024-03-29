package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.controller.*;
import com.catfisher.multiarielle.controller.delta.*;
import com.catfisher.multiarielle.coordinates.TileCoordinate;
import com.catfisher.multiarielle.entity.DrawableEntity;
import com.catfisher.multiarielle.entity.Entity;
import com.catfisher.multiarielle.sprite.Sprite;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor
public abstract class AbstractModel implements Model, DeltaVisitor<Boolean>, DeltaConsumer<Boolean> {

    public abstract Map<Chunk.Address, Chunk> getMap();

    @Getter
    protected Collection<MutablePlacement> allCharacters = new HashSet<>();

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
                    Chunk.Address chunkAddress = Chunk.Address.ofTileCoords(new TileCoordinate(x, y));
                    Pair<Integer, Integer> chunkOffset = Chunk.Address.getOffset(x, y);
                    Chunk chunk = getMap().get(chunkAddress);
                    if  (chunk == null) {
                        toReturn[x-startX][y-startY] = new ArrayList<>();
                        toReturn[x-startX][y-startY].add(Sprite.EMPTY);
                    } else {
                        toReturn[x-startX][y-startY] = new ArrayList<>();
                        toReturn[x - startX][y - startY].add(getMap().get(chunkAddress).
                                getBgLayer()[chunkOffset.getLeft()][chunkOffset.getRight()].getAppearance());
                        toReturn[x - startX][y - startY].add(getMap().get(chunkAddress).
                                getFgLayer()[chunkOffset.getLeft()][chunkOffset.getRight()].getAppearance());
                    }
                }
            }

            // TODO: Oneliner horrible, clean!
            for (Entity entity : getMap().values().stream().map(Chunk -> Chunk.getEntities().values()).flatMap(Collection::stream).collect(Collectors.toList())) {
                if (entity instanceof DrawableEntity) {
                    DrawableEntity drawableEntity = (DrawableEntity) entity;
                    if ((drawableEntity.getX() >= startX) &&
                            (drawableEntity.getX() < endX) &&
                            (drawableEntity.getY() >= startY) &&
                            (drawableEntity.getY() < endY)) {
                        toReturn[drawableEntity.getX() - startX][drawableEntity.getY() - startY].add(drawableEntity.getAppearance());
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
    public Boolean visit(CharacterMoveDelta e) {
        synchronized (this) {
            for (MutablePlacement p : allCharacters) {
                if (p.getCharacter().getName().equals(e.getCharacter().getName())) {
                    int newX = p.getX() + e.getDeltaX();
                    int newY = p.getY() + e.getDeltaY();

                    Chunk.Address newAddress = Chunk.Address.ofTileCoords(new TileCoordinate(newX, newY));
                    if (getMap().get(newAddress) == null) {
                        return false;
                    }

                    BackgroundTile[][] bgLayer = getMap().get(newAddress).getBgLayer();
                    ForegroundTile[][] fgLayer = getMap().get(newAddress).getFgLayer();

                    Pair<Integer, Integer> chunkOffset = Chunk.Address.getOffset(newX, newY);
                    if ((bgLayer[chunkOffset.getLeft()][chunkOffset.getRight()] == null) ||
                            (bgLayer[chunkOffset.getLeft()][chunkOffset.getRight()].isImpassible()) ||
                            (fgLayer[chunkOffset.getLeft()][chunkOffset.getRight()].isImpassible())) {
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

    @Override
    public Boolean visit(CharacterRemoveDelta characterRemoveEvent) {
        allCharacters.removeIf(place -> place.getCharacter().getName().equals(characterRemoveEvent.getCharacter().getName()));
        return true;
    }

    @Override
    public Boolean visit(EntityChangeDelta entityChangeDelta) {
        Chunk oldChunk = getMap().get(entityChangeDelta.getOldChunk());
        if (oldChunk == null) {
            return true;
        }
        oldChunk.getEntities().get(entityChangeDelta.getEntityId()).acceptUpdate(entityChangeDelta.getChange(), this);
        return true;
    }

    @Override
    public Boolean visit(TileChangeDelta tileChangeDelta) {
        Chunk theChunk = getMap().get(Chunk.Address.ofTileCoords(
                new TileCoordinate(
                    tileChangeDelta.getCoordinatees().getX(),
                    tileChangeDelta.getCoordinatees().getY())));
        theChunk.getFgLayer()
                [Chunk.Address.getOffsetX(tileChangeDelta.getCoordinatees())]
                [Chunk.Address.getOffsetY(tileChangeDelta.getCoordinatees())]
                = tileChangeDelta.getTile();
        return true;
    }

    public void moveEntityToChunk(Entity entity, Chunk.Address oldChunk, Chunk.Address newChunk) {
        Chunk fromChunk = getMap().get(oldChunk);
        if (fromChunk != null) {
            fromChunk.getEntities().remove(entity.getId());
        }
        Chunk toChunk = getMap().get(newChunk);
        if (toChunk != null) {
            toChunk.getEntities().put(entity.getId(), entity);
        }
    }

    public boolean isImpassable(int x, int y) {
        Chunk.Address addr = Chunk.Address.ofTileCoords(new TileCoordinate(x, y));
        Chunk chunk = getMap().get(addr);
        if (chunk == null) return true;
        Pair<Integer, Integer> chunkOffset = Chunk.Address.getOffset(x, y);
        return (chunk.getBgLayer()[chunkOffset.getLeft()][chunkOffset.getRight()].isImpassible() ||
                chunk.getFgLayer()[chunkOffset.getLeft()][chunkOffset.getRight()].isImpassible());
    }

    @Override
    public Boolean consume(Delta e) {
        return e.accept(this);
    }
}
