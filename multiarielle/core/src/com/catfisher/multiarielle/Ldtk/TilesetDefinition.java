package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;
import java.util.Map;

/**
 * The `Tileset` definition is the most important part among project definitions. It
 * contains some extra informations about each integrated tileset. If you only had to parse
 * one definition section, that would be the one.
 */
@lombok.Data
public class TilesetDefinition {
    @lombok.Getter(onMethod_ = {@JsonProperty("__cHei")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__cHei")})
    private long cHei;
    @lombok.Getter(onMethod_ = {@JsonProperty("__cWid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__cWid")})
    private long cWid;
    @lombok.Getter(onMethod_ = {@JsonProperty("cachedPixelData")})
    @lombok.Setter(onMethod_ = {@JsonProperty("cachedPixelData")})
    private Map<String, Object> cachedPixelData;
    @lombok.Getter(onMethod_ = {@JsonProperty("customData")})
    @lombok.Setter(onMethod_ = {@JsonProperty("customData")})
    private TileCustomMetadata[] customData;
    @lombok.Getter(onMethod_ = {@JsonProperty("embedAtlas")})
    @lombok.Setter(onMethod_ = {@JsonProperty("embedAtlas")})
    private EmbedAtlas embedAtlas;
    @lombok.Getter(onMethod_ = {@JsonProperty("enumTags")})
    @lombok.Setter(onMethod_ = {@JsonProperty("enumTags")})
    private EnumTagValue[] enumTags;
    @lombok.Getter(onMethod_ = {@JsonProperty("identifier")})
    @lombok.Setter(onMethod_ = {@JsonProperty("identifier")})
    private String identifier;
    @lombok.Getter(onMethod_ = {@JsonProperty("padding")})
    @lombok.Setter(onMethod_ = {@JsonProperty("padding")})
    private long padding;
    @lombok.Getter(onMethod_ = {@JsonProperty("pxHei")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pxHei")})
    private long pxHei;
    @lombok.Getter(onMethod_ = {@JsonProperty("pxWid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pxWid")})
    private long pxWid;
    @lombok.Getter(onMethod_ = {@JsonProperty("relPath")})
    @lombok.Setter(onMethod_ = {@JsonProperty("relPath")})
    private String relPath;
    @lombok.Getter(onMethod_ = {@JsonProperty("savedSelections")})
    @lombok.Setter(onMethod_ = {@JsonProperty("savedSelections")})
    private Map<String, Object>[] savedSelections;
    @lombok.Getter(onMethod_ = {@JsonProperty("spacing")})
    @lombok.Setter(onMethod_ = {@JsonProperty("spacing")})
    private long spacing;
    @lombok.Getter(onMethod_ = {@JsonProperty("tags")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tags")})
    private String[] tags;
    @lombok.Getter(onMethod_ = {@JsonProperty("tagsSourceEnumUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tagsSourceEnumUid")})
    private Long tagsSourceEnumUid;
    @lombok.Getter(onMethod_ = {@JsonProperty("tileGridSize")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tileGridSize")})
    private long tileGridSize;
    @lombok.Getter(onMethod_ = {@JsonProperty("uid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("uid")})
    private long uid;
}
