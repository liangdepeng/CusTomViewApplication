package com.dpdp.testapplication.chart;

import android.graphics.Color;

public class ChartConfig {
    // 是否需要显示坐标轴
    public boolean isNeedXY = true;
    // 是否需要显示 网格线
    public boolean isNeedGridLine = true;
    // 在 isNeedGridLine = true 前提上 是否展示 x方向网格线
    public boolean isShowXGridLine = true;
    // 在 isNeedGridLine = true 前提上 是否展示 x方向网格线
    public boolean isShowYGridLine = true;
    // 是否需要显示 xy轴 刻度 描述
    public boolean isNeedDescription = true;
    // 是否绘制折线图下的阴影
    public boolean isOpenShade = false;
    // 折线图是否需要绘制点
    public boolean isNeedPoint = false;
    // 阴影 渐变色数组 至少两个颜色
    public int[] shadeColors = new int[]{};
    // 是否需要动态绘制
    public boolean isNeedAnim = false;
    // 动态绘制时间(毫秒)
    public long duration = 3000;
    // 文字颜色
    public int textColor = Color.BLACK;
    // 折线颜色
    public int lineColor = Color.BLACK;
    // 图标主题颜色
    public int chartThemeColor = Color.BLACK;
    // 线的粗细 pixel
    public int lineWidth = 2;
    // 描点颜色
    public int pointColor = Color.BLACK;
    // 文字大小  px
    public int textSize = 20;

    public String xTitle = "x标题";
    public String yTitle = "y标题";
    // ...


    public int getChartThemeColor() {
        return chartThemeColor;
    }

    public void setChartThemeColor(int chartThemeColor) {
        this.chartThemeColor = chartThemeColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isNeedXY() {
        return isNeedXY;
    }

    public void setNeedXY(boolean needXY) {
        isNeedXY = needXY;
    }

    public boolean isNeedGridLine() {
        return isNeedGridLine;
    }

    public void setNeedGridLine(boolean needGridLine) {
        isNeedGridLine = needGridLine;
    }

    public boolean isShowXGridLine() {
        return isShowXGridLine;
    }

    public void setShowXGridLine(boolean showXGridLine) {
        isShowXGridLine = showXGridLine;
    }

    public boolean isShowYGridLine() {
        return isShowYGridLine;
    }

    public void setShowYGridLine(boolean showYGridLine) {
        isShowYGridLine = showYGridLine;
    }

    public boolean isNeedDescription() {
        return isNeedDescription;
    }

    public void setNeedDescription(boolean needDescription) {
        isNeedDescription = needDescription;
    }

    public boolean isOpenShade() {
        return isOpenShade;
    }

    public void setOpenShade(boolean openShade) {
        isOpenShade = openShade;
    }

    public boolean isNeedPoint() {
        return isNeedPoint;
    }

    public void setNeedPoint(boolean needPoint) {
        isNeedPoint = needPoint;
    }

    public int[] getShadeColors() {
        return shadeColors;
    }

    public void setShadeColors(int[] shadeColors) {
        this.shadeColors = shadeColors;
    }

    public boolean isNeedAnim() {
        return isNeedAnim;
    }

    public void setNeedAnim(boolean needAnim) {
        isNeedAnim = needAnim;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getxTitle() {
        return xTitle;
    }

    public void setxTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public String getyTitle() {
        return yTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }
}
