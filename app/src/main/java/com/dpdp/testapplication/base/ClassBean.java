package com.dpdp.testapplication.base;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-16
 * <p>
 * Summary:
 * <p>
 * api path:
 */
public class ClassBean {

    private String desc;
    private String jumpUrl;
    private String tag;
    private Class<?> aClass;

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

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }
}
