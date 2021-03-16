package com.dpdp.testapplication.base;

import com.dpdp.testapplication.livelike.LiveLikeTestActivity;
import com.dpdp.testapplication.text.TextActivity;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-16
 * <p>
 * Summary:Activity 枚举类
 */
public enum ClassEnum {

    // 直播效果测试点赞
    ACTIVITY_LIVE_LIKE_TEST("直播效果测试点赞", "jumpUrl", "tag", LiveLikeTestActivity.class),
    // textView 跳动
    ACTIVITY_TEXT_VIEW_DANCE_TEST("textView 跳动","jumpUrl","tag",TextActivity.class);

    private String desc;

    private String jumpUrl;

    private String tag;

    private Class<?> activityClass;

    ClassEnum(String desc, String jumpUrl, String tag, Class<?> activityClass) {
        this.desc = desc;
        this.jumpUrl = jumpUrl;
        this.tag = tag;
        this.activityClass = activityClass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }
}
