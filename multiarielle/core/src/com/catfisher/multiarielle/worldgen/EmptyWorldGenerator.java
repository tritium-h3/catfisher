package com.catfisher.multiarielle.worldgen;

import com.catfisher.multiarielle.model.BackgroundTile;
import com.catfisher.multiarielle.model.Chunk;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyWorldGenerator implements WorldGenerator {
    @Override
    public Chunk generateChunk(Chunk.Address address) {
        Chunk.Builder builder = Chunk.builder();

        for (int i = 0; i < Chunk.SIZE_X; i++) {
            for (int j = 0; j < Chunk.SIZE_Y; j++) {
                builder.insertTile(i, j, BackgroundTile.GRASS);
            }
        }
        return builder.build();
    }
}
