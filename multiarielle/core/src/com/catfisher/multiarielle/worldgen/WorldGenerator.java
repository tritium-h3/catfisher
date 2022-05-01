package com.catfisher.multiarielle.worldgen;

import com.catfisher.multiarielle.model.Chunk;

public interface WorldGenerator {
    Chunk generateChunk(int chunkX, int chunkY);
}
