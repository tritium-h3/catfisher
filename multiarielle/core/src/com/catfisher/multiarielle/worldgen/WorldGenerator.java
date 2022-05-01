package com.catfisher.multiarielle.worldgen;

import com.catfisher.multiarielle.model.Chunk;

public interface WorldGenerator {
    Chunk generateChunk(Chunk.Address address);
}
