package com.catfisher.multiarielle.model;

import com.catfisher.multiarielle.entity.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;

@Log4j2
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Chunk {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;

    @Getter
    private BackgroundTile[][] bgLayer;
    @Getter
    private ForegroundTile[][] fgLayer;
    @Getter
    private Map<String, Entity> entities;

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
        ForegroundTile[][] foreground = new ForegroundTile[loadedXSize][loadedYSize];

        for (int x = 0; x < loadedYSize; x++) {
            for (int y = 0; y < loadedXSize; y++) {
                background[x][y] = loadedOrientation[loadedXSize-y-1][x];
                foreground[x][y] = ForegroundTile.EMPTY;
            }
        }

        return new Chunk(background, foreground, new HashMap<>());
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

        @JsonIgnore
        public int getMinAbsX() {
            return x * SIZE_X;
        }

        @JsonIgnore
        public int getMinAbsY() {
            return y * SIZE_Y;
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
        BackgroundTile[][] bgAccumulator = new BackgroundTile[SIZE_X][SIZE_Y];
        ForegroundTile[][] fgAccumulator = new ForegroundTile[SIZE_X][SIZE_Y];

        public Builder insertBgTile(int x, int y, BackgroundTile tile) {
            bgAccumulator[x][y] = tile;
            return this;
        }

        public Builder insertFgTile(int x, int y, ForegroundTile tile) {
            fgAccumulator[x][y] = tile;
            return this;
        }

        public Chunk build() {
            Map<String, Entity> entities = new HashMap<>();
            for (int x = 0; x < SIZE_X; x++) {
                for (int y = 0; y < SIZE_Y; y++) {
                    if (bgAccumulator[x][y] == null) {
                        bgAccumulator[x][y] = BackgroundTile.EMPTY;
                    }
                    if (fgAccumulator[x][y] == null) {
                        fgAccumulator[x][y] = ForegroundTile.EMPTY;
                    }
                }
            }
            return new Chunk(bgAccumulator, fgAccumulator, entities);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}