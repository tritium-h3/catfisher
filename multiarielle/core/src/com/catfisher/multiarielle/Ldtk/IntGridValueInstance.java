package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * IntGrid value instance
 */
@lombok.Data
public class IntGridValueInstance {
    @lombok.Getter(onMethod_ = {@JsonProperty("coordId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("coordId")})
    private long coordID;
    @lombok.Getter(onMethod_ = {@JsonProperty("v")})
    @lombok.Setter(onMethod_ = {@JsonProperty("v")})
    private long v;
}
