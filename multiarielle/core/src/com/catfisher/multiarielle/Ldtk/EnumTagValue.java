package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * In a tileset definition, enum based tag infos
 */
@lombok.Data
public class EnumTagValue {
    @lombok.Getter(onMethod_ = {@JsonProperty("enumValueId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("enumValueId")})
    private String enumValueID;
    @lombok.Getter(onMethod_ = {@JsonProperty("tileIds")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tileIds")})
    private long[] tileIDS;
}
