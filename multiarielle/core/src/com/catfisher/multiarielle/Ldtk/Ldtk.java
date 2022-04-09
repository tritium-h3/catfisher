package com.multiarielle.skiscratcher.Ldtk;

import com.fasterxml.jackson.annotation.*;

/**
 * This file is a JSON schema of files created by LDtk level editor (https://ldtk.io).
 *
 * This is the root of any Project JSON file. It contains:  - the project settings, - an
 * array of levels, - a group of definitions (that can probably be safely ignored for most
 * users).
 */
@lombok.Data
public class Ldtk {
    @lombok.Getter(onMethod_ = {@JsonProperty("__FORCED_REFS")})
    @lombok.Setter(onMethod_ = {@JsonProperty("__FORCED_REFS")})
    private ForcedRefs forcedRefs;
    @lombok.Getter(onMethod_ = {@JsonProperty("appBuildId")})
    @lombok.Setter(onMethod_ = {@JsonProperty("appBuildId")})
    private double appBuildID;
    @lombok.Getter(onMethod_ = {@JsonProperty("backupLimit")})
    @lombok.Setter(onMethod_ = {@JsonProperty("backupLimit")})
    private long backupLimit;
    @lombok.Getter(onMethod_ = {@JsonProperty("backupOnSave")})
    @lombok.Setter(onMethod_ = {@JsonProperty("backupOnSave")})
    private boolean backupOnSave;
    @lombok.Getter(onMethod_ = {@JsonProperty("bgColor")})
    @lombok.Setter(onMethod_ = {@JsonProperty("bgColor")})
    private String bgColor;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultGridSize")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultGridSize")})
    private long defaultGridSize;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultLevelBgColor")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultLevelBgColor")})
    private String defaultLevelBgColor;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultLevelHeight")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultLevelHeight")})
    private Long defaultLevelHeight;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultLevelWidth")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultLevelWidth")})
    private Long defaultLevelWidth;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultPivotX")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultPivotX")})
    private double defaultPivotX;
    @lombok.Getter(onMethod_ = {@JsonProperty("defaultPivotY")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defaultPivotY")})
    private double defaultPivotY;
    @lombok.Getter(onMethod_ = {@JsonProperty("defs")})
    @lombok.Setter(onMethod_ = {@JsonProperty("defs")})
    private Definitions defs;
    @lombok.Getter(onMethod_ = {@JsonProperty("exportPng")})
    @lombok.Setter(onMethod_ = {@JsonProperty("exportPng")})
    private Boolean exportPNG;
    @lombok.Getter(onMethod_ = {@JsonProperty("exportTiled")})
    @lombok.Setter(onMethod_ = {@JsonProperty("exportTiled")})
    private boolean exportTiled;
    @lombok.Getter(onMethod_ = {@JsonProperty("externalLevels")})
    @lombok.Setter(onMethod_ = {@JsonProperty("externalLevels")})
    private boolean externalLevels;
    @lombok.Getter(onMethod_ = {@JsonProperty("flags")})
    @lombok.Setter(onMethod_ = {@JsonProperty("flags")})
    private Flag[] flags;
    @lombok.Getter(onMethod_ = {@JsonProperty("identifierStyle")})
    @lombok.Setter(onMethod_ = {@JsonProperty("identifierStyle")})
    private IdentifierStyle identifierStyle;
    @lombok.Getter(onMethod_ = {@JsonProperty("imageExportMode")})
    @lombok.Setter(onMethod_ = {@JsonProperty("imageExportMode")})
    private ImageExportMode imageExportMode;
    @lombok.Getter(onMethod_ = {@JsonProperty("jsonVersion")})
    @lombok.Setter(onMethod_ = {@JsonProperty("jsonVersion")})
    private String jsonVersion;
    @lombok.Getter(onMethod_ = {@JsonProperty("levelNamePattern")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levelNamePattern")})
    private String levelNamePattern;
    @lombok.Getter(onMethod_ = {@JsonProperty("levels")})
    @lombok.Setter(onMethod_ = {@JsonProperty("levels")})
    private Level[] levels;
    @lombok.Getter(onMethod_ = {@JsonProperty("minifyJson")})
    @lombok.Setter(onMethod_ = {@JsonProperty("minifyJson")})
    private boolean minifyJSON;
    @lombok.Getter(onMethod_ = {@JsonProperty("nextUid")})
    @lombok.Setter(onMethod_ = {@JsonProperty("nextUid")})
    private long nextUid;
    @lombok.Getter(onMethod_ = {@JsonProperty("pngFilePattern")})
    @lombok.Setter(onMethod_ = {@JsonProperty("pngFilePattern")})
    private String pngFilePattern;
    @lombok.Getter(onMethod_ = {@JsonProperty("simplifiedExport")})
    @lombok.Setter(onMethod_ = {@JsonProperty("simplifiedExport")})
    private boolean simplifiedExport;
    @lombok.Getter(onMethod_ = {@JsonProperty("tutorialDesc")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tutorialDesc")})
    private String tutorialDesc;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldGridHeight")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldGridHeight")})
    private Long worldGridHeight;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldGridWidth")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldGridWidth")})
    private Long worldGridWidth;
    @lombok.Getter(onMethod_ = {@JsonProperty("worldLayout")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worldLayout")})
    private WorldLayout worldLayout;
    @lombok.Getter(onMethod_ = {@JsonProperty("worlds")})
    @lombok.Setter(onMethod_ = {@JsonProperty("worlds")})
    private World[] worlds;
}
