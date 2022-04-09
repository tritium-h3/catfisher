package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class EntityInstance {
    @lombok.Getter(onMethod_ = {@JsonProperty("__grid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__grid")})
    private long[] grid;
    @lombok.Getter(onMethod_ = {@JsonProperty("__identifier")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__identifier")})
    private String identifier;
    @lombok.Getter(onMethod_ = {@JsonProperty("__pivot")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__pivot")})
    private double[] pivot;
    @lombok.Getter(onMethod_ = {@JsonProperty("__smartColor")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__smartColor")})
    private String smartColor;
    @lombok.Getter(onMethod_ = {@JsonProperty("__tags")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__tags")})
    private String[] tags;
    @lombok.Getter(onMethod_ = {@JsonProperty("__tile")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__tile")})
    private TilesetRectangle tile;
    @lombok.Getter(onMethod_ = {@JsonProperty("defUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defUid")})
    private long defUid;
    @lombok.Getter(onMethod_ = {@JsonProperty("fieldInstances")})
    @lombok.Setter(onMethod_ = {@JsonProperty("fieldInstances")})
    private FieldInstance[] fieldInstances;
    @lombok.Getter(onMethod_ = {@JsonProperty("height")})
    @lombok.Setter(onMethod_ = {@JsonProperty("height")})
    private long height;
    @lombok.Getter(onMethod_ = {@JsonProperty("iid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("iid")})
    private String iid;
    @lombok.Getter(onMethod_ = {@JsonProperty("px")})
    @lombok.Setter(onMethod_ = {@JsonProperty("px")})
    private long[] px;
    @lombok.Getter(onMethod_ = {@JsonProperty("width")})
    @lombok.Setter(onMethod_ = {@JsonProperty("width")})
    private long width;
}
