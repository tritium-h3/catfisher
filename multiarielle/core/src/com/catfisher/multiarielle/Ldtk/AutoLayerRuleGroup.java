package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

@lombok.Data
public class AutoLayerRuleGroup {
    @lombok.Getter(onMethod_ = {@JsonProperty("active")})
    @lombok.Setter(onMethod_ = {@JsonProperty("active")})
    private boolean active;
    @lombok.Getter(onMethod_ = {@JsonProperty("collapsed")})
    @lombok.Setter(onMethod_ = {@JsonProperty("collapsed")})
    private Boolean collapsed;
    @lombok.Getter(onMethod_ = {@JsonProperty("isOptional")})
    @lombok.Setter(onMethod_ = {@JsonProperty("isOptional")})
    private boolean isOptional;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("rules")})
    @lombok.Setter(onMethod_ = {@JsonProperty("rules")})
    private AutoLayerRuleDefinition[] rules;
    @lombok.Getter(onMethod_ = {@JsonProperty("uid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("uid")})
    private long uid;
}
