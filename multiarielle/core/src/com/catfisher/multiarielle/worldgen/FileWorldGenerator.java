package com.catfisher.multiarielle.worldgen;

import com.catfisher.multiarielle.model.Chunk;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileWorldGenerator implements WorldGenerator {
    private final Map<Chunk.Address, Chunk> chunkMap = new HashMap<>();

    public FileWorldGenerator(String fileName) throws IOException {
        String csv = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        chunkMap.put(new Chunk.Address(0, 0), Chunk.readFromCSV(csv));
        chunkMap.put(new Chunk.Address(0, -1), Chunk.readFromCSV(csv));
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkY) {
        return chunkMap.get(new Chunk.Address(chunkX, chunkY));
    }
}
