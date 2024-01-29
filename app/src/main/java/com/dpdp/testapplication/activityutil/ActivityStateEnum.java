package com.dpdp.testapplication.activityutil;

public enum ActivityStateEnum {

    ON_CREATE("onCreate"),
    ON_START("onStart"),
    ON_RESUME("onResume"),
    ON_PAUSE("onPause"),
    ON_STOP("onStop"),
    ON_DESTROY("onDestroy");

    String methodName;

    ActivityStateEnum(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
