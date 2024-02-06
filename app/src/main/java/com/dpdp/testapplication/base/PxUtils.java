package com.dpdp.testapplication.base;

import com.dpdp.testapplication.BaseApp;

public class PxUtils {

    public static int dp2px(float dpValue) {
        final float scale = BaseApp.globalContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
