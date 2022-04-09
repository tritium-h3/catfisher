package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class FieldInstance {
    @lombok.Getter(onMethod_ = {@JsonProperty("__identifier")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__identifier")})
    private String identifier;
    @lombok.Getter(onMethod_ = {@JsonProperty("__tile")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__tile")})
    private TilesetRectangle tile;
    @lombok.Getter(onMethod_ = {@JsonProperty("__type")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__type")})
    private String type;
    @lombok.Getter(onMethod_ = {@JsonProperty("__value")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__value")})
    private Object value;
    @lombok.Getter(onMethod_ = {@JsonProperty("defUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defUid")})
    private long defUid;
    @lombok.Getter(onMethod_ = {@JsonProperty("realEditorValues")})
    @lombok.Setter(onMethod_ = {@JsonProperty("realEditorValues")})
    private Object[] realEditorValues;
}
