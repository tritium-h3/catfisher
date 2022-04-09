package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This object is used in Field Instances to describe an EntityRef value.
 */
@lombok.Data
public class FieldInstanceEntityReference {
    @lombok.Getter(onMethod_ = {@JsonProperty("entityIid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("entityIid")})
    private String entityIid;
    @lombok.Getter(onMethod_ = {@JsonProperty("layerIid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("layerIid")})
    private String layerIid;
    @lombok.Getter(onMethod_ = {@JsonProperty("levelIid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levelIid")})
    private String levelIid;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldIid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldIid")})
    private String worldIid;
}
