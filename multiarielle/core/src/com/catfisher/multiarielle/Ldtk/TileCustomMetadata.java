package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * In a tileset definition, user defined meta-data of a tile.
 */
@lombok.Data
public class TileCustomMetadata {
    @lombok.Getter(onMethod_ = {@JsonProperty("data")})
    @lombok.Setter(onMethod_ = {@JsonProperty("data")})
    private String data;
    @lombok.Getter(onMethod_ = {@JsonProperty("tileId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tileId")})
    private long tileID;
}
