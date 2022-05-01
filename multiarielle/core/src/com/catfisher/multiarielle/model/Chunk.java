package com.catfisher.multiarielle.model;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;

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

    @Data
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class Address {
        int x;
        int y;

        public static Address ofAbsoluteCoords(int absX, int absY) {
            return new Address(Math.floorDiv(absX, SIZE_X), Math.floorDiv(absY, SIZE_Y));
        }

        public static Pair<Integer, Integer> getOffset(int absX, int absY) {
            return new ImmutablePair<>(Math.floorMod(absX, SIZE_X), Math.floorMod(absY, SIZE_Y));
        }

        public static class KeySer extends StdKeySerializers.StringKeySerializer {
            @Override
            public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
                Address addr = (Address)value;
                g.writeFieldName(addr.getX() + "," + addr.getY());
            }
        }

        public static class KeyDeser extends KeyDeserializer {
            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
                String[] fields = key.split(",");
                return new Address(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]));
            }
        }
    }

    public static class Builder {
        BackgroundTile[][] accumulator = new BackgroundTile[SIZE_X][SIZE_Y];

        public Builder insertTile(int x, int y, BackgroundTile tile) {
            accumulator[x][y] = tile;
            return this;
        }

        public Chunk build() {
            return new Chunk(accumulator);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}