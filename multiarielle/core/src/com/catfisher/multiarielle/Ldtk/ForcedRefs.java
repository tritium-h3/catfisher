package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This object is not actually used by LDtk. It ONLY exists to force explicit references to
 * all types, to make sure QuickType finds them and integrate all of them. Otherwise,
 * Quicktype will drop types that are not explicitely used.
 */
@lombok.Data
public class ForcedRefs {
    @lombok.Getter(onMethod_ = {@JsonProperty("AutoLayerRuleGroup")})
    @lombok.Setter(onMethod_ = {@JsonProperty("AutoLayerRuleGroup")})
    private AutoLayerRuleGroup autoLayerRuleGroup;
    @lombok.Getter(onMethod_ = {@JsonProperty("AutoRuleDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("AutoRuleDef")})
    private AutoLayerRuleDefinition autoRuleDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("Definitions")})
    @lombok.Setter(onMethod_ = {@JsonProperty("Definitions")})
    private Definitions definitions;
    @lombok.Getter(onMethod_ = {@JsonProperty("EntityDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EntityDef")})
    private EntityDefinition entityDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("EntityInstance")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EntityInstance")})
    private EntityInstance entityInstance;
    @lombok.Getter(onMethod_ = {@JsonProperty("EntityReferenceInfos")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EntityReferenceInfos")})
    private FieldInstanceEntityReference entityReferenceInfos;
    @lombok.Getter(onMethod_ = {@JsonProperty("EnumDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EnumDef")})
    private EnumDefinition enumDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("EnumDefValues")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EnumDefValues")})
    private EnumValueDefinition enumDefValues;
    @lombok.Getter(onMethod_ = {@JsonProperty("EnumTagValue")})
    @lombok.Setter(onMethod_ = {@JsonProperty("EnumTagValue")})
    private EnumTagValue enumTagValue;
    @lombok.Getter(onMethod_ = {@JsonProperty("FieldDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("FieldDef")})
    private FieldDefinition fieldDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("FieldInstance")})
    @lombok.Setter(onMethod_ = {@JsonProperty("FieldInstance")})
    private FieldInstance fieldInstance;
    @lombok.Getter(onMethod_ = {@JsonProperty("GridPoint")})
    @lombok.Setter(onMethod_ = {@JsonProperty("GridPoint")})
    private FieldInstanceGridPoint gridPoint;
    @lombok.Getter(onMethod_ = {@JsonProperty("IntGridValueDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("IntGridValueDef")})
    private IntGridValueDefinition intGridValueDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("IntGridValueInstance")})
    @lombok.Setter(onMethod_ = {@JsonProperty("IntGridValueInstance")})
    private IntGridValueInstance intGridValueInstance;
    @lombok.Getter(onMethod_ = {@JsonProperty("LayerDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("LayerDef")})
    private LayerDefinition layerDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("LayerInstance")})
    @lombok.Setter(onMethod_ = {@JsonProperty("LayerInstance")})
    private LayerInstance layerInstance;
    @lombok.Getter(onMethod_ = {@JsonProperty("Level")})
    @lombok.Setter(onMethod_ = {@JsonProperty("Level")})
    private Level level;
    @lombok.Getter(onMethod_ = {@JsonProperty("LevelBgPosInfos")})
    @lombok.Setter(onMethod_ = {@JsonProperty("LevelBgPosInfos")})
    private LevelBackgroundPosition levelBgPosInfos;
    @lombok.Getter(onMethod_ = {@JsonProperty("NeighbourLevel")})
    @lombok.Setter(onMethod_ = {@JsonProperty("NeighbourLevel")})
    private NeighbourLevel neighbourLevel;
    @lombok.Getter(onMethod_ = {@JsonProperty("Tile")})
    @lombok.Setter(onMethod_ = {@JsonProperty("Tile")})
    private TileInstance tile;
    @lombok.Getter(onMethod_ = {@JsonProperty("TileCustomMetadata")})
    @lombok.Setter(onMethod_ = {@JsonProperty("TileCustomMetadata")})
    private TileCustomMetadata tileCustomMetadata;
    @lombok.Getter(onMethod_ = {@JsonProperty("TilesetDef")})
    @lombok.Setter(onMethod_ = {@JsonProperty("TilesetDef")})
    private TilesetDefinition tilesetDef;
    @lombok.Getter(onMethod_ = {@JsonProperty("TilesetRect")})
    @lombok.Setter(onMethod_ = {@JsonProperty("TilesetRect")})
    private TilesetRectangle tilesetRect;
    @lombok.Getter(onMethod_ = {@JsonProperty("World")})
    @lombok.Setter(onMethod_ = {@JsonProperty("World")})
    private World world;
}
