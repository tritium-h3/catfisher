package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This structure represents a single tile from a given Tileset.
 */
@lombok.Data
public class TileInstance {
    @lombok.Getter(onMethod_ = {@JsonProperty("d")})
    @lombok.Setter(onMethod_ = {@JsonProperty("d")})
    private long[] d;
    @lombok.Getter(onMethod_ = {@JsonProperty("f")})
    @lombok.Setter(onMethod_ = {@JsonProperty("f")})
    private long f;
    @lombok.Getter(onMethod_ = {@JsonProperty("px")})
    @lombok.Setter(onMethod_ = {@JsonProperty("px")})
    private long[] px;
    @lombok.Getter(onMethod_ = {@JsonProperty("src")})
    @lombok.Setter(onMethod_ = {@JsonProperty("src")})
    private long[] src;
    @lombok.Getter(onMethod_ = {@JsonProperty("t")})
    @lombok.Setter(onMethod_ = {@JsonProperty("t")})
    private long t;
}
