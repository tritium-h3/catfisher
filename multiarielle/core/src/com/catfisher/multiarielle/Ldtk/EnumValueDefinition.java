package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class EnumValueDefinition {
    @lombok.Getter(onMethod_ = {@JsonProperty("__tileSrcRect")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__tileSrcRect")})
    private long[] tileSrcRect;
    @lombok.Getter(onMethod_ = {@JsonProperty("color")})
    @lombok.Setter(onMethod_ = {@JsonProperty("color")})
    private long color;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    private String id;
    @lombok.Getter(onMethod_ = {@JsonProperty("tileId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tileId")})
    private Long tileID;
}
