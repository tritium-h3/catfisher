package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This object is just a grid-based coordinate used in Field values.
 */
@lombok.Data
public class FieldInstanceGridPoint {
    @lombok.Getter(onMethod_ = {@JsonProperty("cx")})
    @lombok.Setter(onMethod_ = {@JsonProperty("cx")})
    private long cx;
    @lombok.Getter(onMethod_ = {@JsonProperty("cy")})
    @lombok.Setter(onMethod_ = {@JsonProperty("cy")})
    private long cy;
}
