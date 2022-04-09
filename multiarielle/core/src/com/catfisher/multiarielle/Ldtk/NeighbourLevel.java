package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * Nearby level info
 */
@lombok.Data
public class NeighbourLevel {
    @lombok.Getter(onMethod_ = {@JsonProperty("dir")})
    @lombok.Setter(onMethod_ = {@JsonProperty("dir")})
    private String dir;
    @lombok.Getter(onMethod_ = {@JsonProperty("levelIid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levelIid")})
    private String levelIid;
    @lombok.Getter(onMethod_ = {@JsonProperty("levelUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levelUid")})
    private Long levelUid;
}
