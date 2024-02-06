package com.dpdp.testapplication.base;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.dpdp.testapplication.BaseApp;

public class ScreenUtils {

    public static int getScreenWidth() {
        int screenWidth = BaseApp.globalContext.getResources().getDisplayMetrics().widthPixels;
        if (screenWidth == 0) {
            Point screen = new Point();
            WindowManager windowManager = (WindowManager) BaseApp.globalContext.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                display.getRealSize(screen);
                screenWidth = screen.x;
            }
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        int screenHeight = BaseApp.globalContext.getResources().getDisplayMetrics().heightPixels;
        if (screenHeight == 0) {
            Point screen = new Point();
            WindowManager windowManager = (WindowManager) BaseApp.globalContext.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                Display display = windowManager.getDefaultDisplay();
                display.getRealSize(screen);
                screenHeight = screen.y;
            }
        }
        return screenHeight;
    }
}
