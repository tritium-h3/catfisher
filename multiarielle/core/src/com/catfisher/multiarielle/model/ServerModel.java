package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.worldgen.WorldGenerator;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

public class ServerModel extends AbstractModel {
    private final WorldGenerator generator;
    private final Map<Chunk.Address, Chunk> map;

    public ServerModel(WorldGenerator generator) {
        this.generator = generator;
        map = LazyMap.lazyMap(
                new HashMap<>(),
                key -> generator.generateChunk(key.getX(), key.getY())
        );
        map.get(new Chunk.Address(0, 0));
        map.get(new Chunk.Address(0, -1));
    }

    @Override
    public Map<Chunk.Address, Chunk> getMap() {
        return map;
    }
}
