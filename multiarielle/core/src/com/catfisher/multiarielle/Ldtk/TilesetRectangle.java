package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This object represents a custom sub rectangle in a Tileset image.
 */
@lombok.Data
public class TilesetRectangle {
    @lombok.Getter(onMethod_ = {@JsonProperty("h")})
    @lombok.Setter(onMethod_ = {@JsonProperty("h")})
    private long h;
    @lombok.Getter(onMethod_ = {@JsonProperty("tilesetUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tilesetUid")})
    private long tilesetUid;
    @lombok.Getter(onMethod_ = {@JsonProperty("w")})
    @lombok.Setter(onMethod_ = {@JsonProperty("w")})
    private long w;
    @lombok.Getter(onMethod_ = {@JsonProperty("x")})
    @lombok.Setter(onMethod_ = {@JsonProperty("x")})
    private long x;
    @lombok.Getter(onMethod_ = {@JsonProperty("y")})
    @lombok.Setter(onMethod_ = {@JsonProperty("y")})
    private long y;
}
