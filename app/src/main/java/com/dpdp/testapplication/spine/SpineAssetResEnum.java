package com.dpdp.testapplication.spine;

/**
 * Spine 动画 本地资源集合
 * Date: 2024/6/7 16:14
 * Author: liangdp
 */
public enum SpineAssetResEnum {

    HERO("hero", "testspine/hero.atlas", "testspine/hero.json"),
    CAT("cat", "testspine/matching.atlas", "testspine/matching.json"),
    STRETCHY_MAN("stretchy_man", "testspine/stretchyman.atlas", "testspine/stretchyman.json");


    final String name;
    final String atlasPath;
    final String jsonPath;

    SpineAssetResEnum(String name, String atlasPath, String jsonPath) {
        this.name = name;
        this.atlasPath = atlasPath;
        this.jsonPath = jsonPath;
    }

    public String getName() {
        return name;
    }

    public String getAtlasPath() {
        return atlasPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    //  TODO 通过 name 对应动本地资源
    public static SpineAssetResEnum getSpineAssetsByName(String name) {
        switch (name) {
            case "hero":
                return HERO;
            case "cat":
                return CAT;
            case "stretchy_man":
                return STRETCHY_MAN;
            default:
                return null;
        }
    }
}
