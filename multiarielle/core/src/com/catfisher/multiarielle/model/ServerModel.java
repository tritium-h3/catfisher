package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.worldgen.WorldGenerator;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public class ServerModel extends AbstractModel {
    private static final int SUBSCRIPTION_RADIUS = 3;

    private final WorldGenerator generator;
    private final Map<Chunk.Address, Chunk> map;

    public ServerModel(WorldGenerator generator) {
        this.generator = generator;
        map = LazyMap.lazyMap(
                new HashMap<>(),
                key -> generator.generateChunk(key.getX(), key.getY())
        );
    }

    @Override
    public Map<Chunk.Address, Chunk> getMap() {
        return map;
    }

    public Map<Chunk.Address, Chunk> getSubMapAround(Chunk.Address center) {
        Map<Chunk.Address, Chunk> toReturn = new HashMap<>();
        for (int i = center.getX() - (SUBSCRIPTION_RADIUS - 1); i < center.getX() + SUBSCRIPTION_RADIUS; i++) {
            for (int j = center.getY() - (SUBSCRIPTION_RADIUS - 1); j < center.getY() + SUBSCRIPTION_RADIUS; j++) {
                Chunk.Address address = new Chunk.Address(i, j);
                Chunk chunk = map.get(address);
                if (chunk != null) {
                    toReturn.put(address, map.get(address));
                }
            }
        }
        return toReturn;
    }
}
