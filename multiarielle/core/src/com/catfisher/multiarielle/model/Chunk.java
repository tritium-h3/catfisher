package com.catfisher.multiarielle.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Log4j2
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Chunk {
    @Getter
    private BackgroundTile[][] bgLayer;

    public static Chunk readFromCSV(String csv) {
        List<BackgroundTile[]> lines = new ArrayList<>();
        String line;
        Scanner scanner = new Scanner(csv);

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] values = line.split(",");
            BackgroundTile[] tileLine =
                    Arrays.stream(values).map(
                            val -> BackgroundTile.values()[Integer.parseInt(val)]
                    ).toArray(BackgroundTile[]::new);
            lines.add(tileLine);
        }

        BackgroundTile[][] loadedOrientation = lines.toArray(new BackgroundTile[0][0]);
        BackgroundTile[][] background;
        int loadedYSize = loadedOrientation[0].length;
        int loadedXSize = loadedOrientation.length;

        background = new BackgroundTile[loadedYSize][loadedXSize];

        for (int x = 0; x < loadedYSize; x++) {
            for (int y = 0; y < loadedXSize; y++) {
                background[x][y] = loadedOrientation[loadedXSize-y-1][x];
            }
        }

        return new Chunk(background);
    }

    public static Chunk readFromFile(String filename) {
        try {
            return readFromCSV(new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("IOException: ", e);
            return null;
        }
    }
};

