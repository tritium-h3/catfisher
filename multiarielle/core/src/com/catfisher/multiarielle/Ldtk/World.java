package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * **IMPORTANT**: this type is not used *yet* in current LDtk version. It's only presented
 * here as a preview of a planned feature.  A World contains multiple levels, and it has its
 * own layout settings.
 */
@lombok.Data
public class World {
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultLevelHeight")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultLevelHeight")})
    private long defaultLevelHeight;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultLevelWidth")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultLevelWidth")})
    private long defaultLevelWidth;
    @lombok.Getter(onMethod_ = {@JsonProperty("identifier")})
    @lombok.Setter(onMethod_ = {@JsonProperty("identifier")})
    private String identifier;
    @lombok.Getter(onMethod_ = {@JsonProperty("iid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("iid")})
    private String iid;
    @lombok.Getter(onMethod_ = {@JsonProperty("levels")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levels")})
    private Level[] levels;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldGridHeight")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldGridHeight")})
    private long worldGridHeight;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldGridWidth")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldGridWidth")})
    private long worldGridWidth;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldLayout")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldLayout")})
    private WorldLayout worldLayout;
}
