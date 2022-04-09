package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * Level background image position info
 */
@lombok.Data
public class LevelBackgroundPosition {
    @lombok.Getter(onMethod_ = {@JsonProperty("cropRect")})
    @lombok.Setter(onMethod_ = {@JsonProperty("cropRect")})
    private double[] cropRect;
    @lombok.Getter(onMethod_ = {@JsonProperty("scale")})
    @lombok.Setter(onMethod_ = {@JsonProperty("scale")})
    private double[] scale;
    @lombok.Getter(onMethod_ = {@JsonProperty("topLeftPx")})
    @lombok.Setter(onMethod_ = {@JsonProperty("topLeftPx")})
    private long[] topLeftPx;
}
