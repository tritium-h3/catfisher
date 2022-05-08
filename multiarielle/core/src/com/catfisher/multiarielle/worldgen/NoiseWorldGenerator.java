package com.catfisher.multiarielle.worldgen;

import com.catfisher.multiarielle.model.BackgroundTile;
import com.catfisher.multiarielle.model.Chunk;
import lombok.extern.log4j.Log4j2;
import org.spongepowered.noise.NoiseQuality;
import org.spongepowered.noise.module.NoiseModule;
import org.spongepowered.noise.module.source.Perlin;

import java.util.Random;

@Log4j2
public class NoiseWorldGenerator implements WorldGenerator {
    public static double SEA_LEVEL = 0.4;
    private final Random random = new Random();
    private final Perlin noise;

    public NoiseWorldGenerator() {
        noise = new Perlin();
        noise.setSeed(random.nextInt());
        noise.setFrequency(0.2);
        noise.setLacunarity(2.0);
        noise.setNoiseQuality(NoiseQuality.STANDARD);
        noise.setPersistence(0.5);
        noise.setOctaveCount(1);
    }

    @Override
    public Chunk generateChunk(Chunk.Address address) {
        Chunk.Builder builder = Chunk.builder();
        for (int i = 0; i < Chunk.SIZE_X; i++) {
            for (int j = 0; j < Chunk.SIZE_Y; j++) {
                double x = address.getMinAbsX() + i;
                double y = address.getMinAbsY() + j;
                double hmap = noise.get(x, y, 500.0);
                builder.insertTile(i, j, hmap < SEA_LEVEL ? BackgroundTile.WATER : BackgroundTile.GRASS);
            }
        }
        return builder.build();
    }
}
