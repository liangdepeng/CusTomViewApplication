package com.os.android.spinelib.spine;

/**
 * Spine 动画 本地资源集合
 * Date: 2024/6/6 16:14
 * Author: liangdp
 */
public enum SpineAssetResEnum {

    GAME_HAGD("hagd","hagdspine/guandan.atlas","hagdspine/guandan.json"),
    GAME_LYBJ("lybj","lybjspine/biji.atlas","lybjspine/biji.json"),
    BG_START_GAME("开始按钮","startgame/start_glow.atlas","startgame/start_glow.json"),
    BG_REWARD("奖励背景","reward/gongxihuode.atlas","reward/gongxihuode.json");


    String name;
    String atlasPath;
    String jsonPath;
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

    // 通过 name 对应游戏动本地资源
    public static SpineAssetResEnum getGameSpineAssetsByName(String name){
        switch (name){
            case "hagd":
                return GAME_HAGD;
            case "lybj":
                return GAME_LYBJ;
            default:
                return null;
        }
    }
}
