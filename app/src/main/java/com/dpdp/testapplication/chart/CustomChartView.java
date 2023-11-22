package com.dpdp.testapplication.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.Nullable;

import com.dpdp.testapplication.BaseApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线图
 * <p>
 * Date: 2023/11/1 14:48
 * Author: liangdp
 */
public class CustomChartView extends View {

    // 画笔
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mPointPaint;
    private Paint mShaderPaint;
    // 路径
    private Path mLinePath;
    private Path mShaderPath;

    private int mTextSize = 20;
    // 原点坐标  注意android原点是在左上角
    // 这里的坐标原点是坐标系的
    private int originPointX;
    private int originPointY;
    //y方向顶部的点y坐标
    private int yTopPoint;

    private final ArrayList<String> xDatas = new ArrayList<>();
    private final ArrayList<Integer> yDatas = new ArrayList<>();
    private final List<Integer> chartData = new ArrayList<>();
    private int xItemDistance;
    private int yItemDistance;
    private int mWidth;
    private int mHeight;
    private int yMax;
    private int yMin;
    private int progress;
    private int yAllLength;
    private ChartConfig chartConfig = new ChartConfig();

    public CustomChartView(Context context) {
        this(context, null);
    }

    public CustomChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig();
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    // 坐标
    public void setData(List<String> xList, List<Integer> yList, List<Integer> chartYList, ChartConfig config) {
        if (xList != null && yList != null) {
            xDatas.clear();
            xDatas.addAll(xList);
            yDatas.clear();
            yDatas.addAll(yList);
        }
        if (chartYList != null) {
            chartData.clear();
            chartData.addAll(chartYList);
        }

        if (config != null) {
            chartConfig = config;
        }

        if (chartConfig.chartThemeColor != 0) {
            mPaint.setColor(chartConfig.chartThemeColor);
        }

        if (chartConfig.lineColor != 0) {
            mLinePaint.setColor(chartConfig.lineColor);
        }

        if (chartConfig.lineWidth > 0) {
            mLinePaint.setStrokeWidth(chartConfig.lineWidth);
        }

        if (chartConfig.pointColor != 0) {
            mPointPaint.setColor(chartConfig.pointColor);
        }

        if (chartConfig.textSize > 0) {
            mTextSize = chartConfig.textSize;
            mTextPaint.setTextSize(chartConfig.textSize);
        }

        if (chartConfig.textColor != 0) {
            mTextPaint.setColor(chartConfig.textColor);
        }

        // 计算 Y轴数据范围
        if (yDatas.size() > 0) {
            yMax = yDatas.get(0);
            yMin = yDatas.get(0);
        }

        for (int i = 0; i < yDatas.size(); i++) {
            int data = yDatas.get(i);
            if (data > yMax) {
                yMax = data;
            } else if (data < yMin) {
                yMin = data;
            }
        }
        yAllLength = yMax - yMin;

        // 绘制 是否需要动态绘制
        if (chartConfig.isNeedAnim) {
            ValueAnimator animator = ValueAnimator.ofInt(0, 100).setDuration(chartConfig.duration);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
            return;
        }
        invalidate();
    }

    private void initConfig() {
        // 画坐标轴
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        //  mPaint.setTextSize(chartConfig.textSize);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(chartConfig.textColor);
        mTextPaint.setTextSize(chartConfig.textSize);

        // 折线图画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(chartConfig.lineColor);
        mLinePaint.setStrokeWidth(dp2px(2));


        // 画小圆点,折线上的每一个点
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setColor(chartConfig.pointColor);

        //阴影画笔
        mShaderPaint = new Paint();
        mShaderPaint.setAntiAlias(true);
        mShaderPaint.setStrokeWidth(2f);


        // 折线图路径
        mLinePath = new Path();
        mShaderPath = new Path();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 清除上一次的路径 否则会和上一次的路径重叠，同时存在
        mLinePath.reset();
        mShaderPath.reset();

        // 原点坐标x,y
        originPointX = dp2px(chartConfig.textSize);
        originPointY = mHeight - dp2px(chartConfig.textSize);

        // y轴最上面的点y坐标
        yTopPoint = dp2px(chartConfig.textSize);


        if (xDatas.size() > 0) {
            // x 轴 间隔距离
            xItemDistance = (mWidth - 2 * originPointX) / xDatas.size();
        }

        if (yDatas.size() > 0) {
            // y 轴 间隔距离
            yItemDistance = (int) ((originPointY - yTopPoint - mTextSize) / yDatas.size() * 1.0);
        }

        drawAxes(canvas);
        drawText(canvas);
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        if (chartData.size() == 0)
            return;
        // 计算 y 轴的绘制刻度的部分总长度
        int yLength = originPointY - yTopPoint - mTextSize - yItemDistance;
        for (int i = 0; i < chartData.size(); i++) {
            int integer = chartData.get(i);
            // 计算百分比 乘以 y轴长度 计算相对长度
            int tempItemY = (int) (((integer * 1.0 - yMin) / yAllLength) * yLength * 1.0);

            if (chartConfig.isNeedPoint) {
                // 绘制点
                canvas.drawCircle(originPointX + i * xItemDistance, originPointY - tempItemY, dp2px(1f), mPointPaint);
            }

            if (i == 0) {
                // 折线图 开始
                mLinePath.moveTo(originPointX, originPointY - tempItemY);
                if (chartConfig.isOpenShade) {
                    // 阴影，如果有的话
                    mShaderPath.moveTo(originPointX, originPointY - tempItemY);
                }
            } else {
                // 依次连接每个点
                mLinePath.lineTo(originPointX + i * xItemDistance, originPointY - tempItemY);
                if (chartConfig.isOpenShade) {
                    mShaderPath.lineTo(originPointX + i * xItemDistance, originPointY - tempItemY);
                    // 要实现阴影的话 需要多加两个点 一个垂直向下落到x轴上，再加一个连接回到原点
                    if (i == chartData.size() - 1) {
                        mShaderPath.lineTo(originPointX + i * xItemDistance, originPointY);
                        mShaderPath.lineTo(originPointX, originPointY);
                        mShaderPath.close();
                    }
                }
            }
        }

        if (chartConfig.isNeedAnim) {
            // 按照动画进度绘制  progress 控制进度不断重绘
            setPathAnimation(canvas, mLinePath, progress, mLinePaint);
        } else {
            canvas.drawPath(mLinePath, mLinePaint);
        }

        if (chartConfig.isOpenShade) {
            if (chartConfig.isNeedAnim) {
                if (progress > 95) {
                    // 折线图添加动画的话 阴影这边需要稍微做一个动画效果处理一下  体验比闪现更好
                    Shader linearGradient = new LinearGradient(0, 0, 0, getHeight(), chartConfig.shadeColors, null, Shader.TileMode.CLAMP);
                    mShaderPaint.setShader(linearGradient);
                    mShaderPaint.setAlpha((int) (255 * (progress - 95) / 20f));
                    canvas.drawPath(mShaderPath, mShaderPaint);
                }
            } else {
                Shader linearGradient = new LinearGradient(0, 0, 0, getHeight(), chartConfig.shadeColors, null, Shader.TileMode.CLAMP);
                mShaderPaint.setShader(linearGradient);
                canvas.drawPath(mShaderPath, mShaderPaint);
            }
        }
    }

    private void drawText(Canvas canvas) {
        if (chartConfig.isNeedDescription) {
            float xTitleWidth = mTextPaint.measureText(chartConfig.xTitle);
            // x轴 title
            canvas.drawText(chartConfig.xTitle, mWidth - originPointX - xTitleWidth / 2, originPointY + dp2px(3f) + mTextSize, mTextPaint);
            // y轴 title
            canvas.drawText(chartConfig.yTitle, originPointX + dp2px(6f), yTopPoint + mTextSize / 2, mTextPaint);

            for (int i = 0; i < xDatas.size(); i++) {
                // x轴 文字
                int tempX = (int) (originPointX + i * xItemDistance - mTextPaint.measureText(xDatas.get(i)) / 2);
                canvas.drawText(xDatas.get(i), tempX, originPointY + dp2px(3f) + mTextSize, mTextPaint);
            }

            for (int i = 0; i < yDatas.size(); i++) {
                // y轴 文字
                int tempY = originPointY - i * yItemDistance;
                canvas.drawText(String.valueOf(yDatas.get(i)), originPointX - mTextPaint.measureText(String.valueOf(yDatas.get(i))) - dp2px(3f), tempY, mTextPaint);
            }
        }
    }

    private void drawAxes(Canvas canvas) {
        if (chartConfig.isNeedXY) {
            // x 轴
            canvas.drawLine(originPointX, originPointY, mWidth - originPointX, originPointY, mPaint);
            // x轴最右边的箭头
            canvas.drawLine(mWidth - originPointX, originPointY, mWidth - originPointX - dp2px(6f), originPointY + dp2px(3f), mPaint);
            canvas.drawLine(mWidth - originPointX, originPointY, mWidth - originPointX - dp2px(6f), originPointY - dp2px(3f), mPaint);

        }

        if (chartConfig.isNeedGridLine && chartConfig.isShowXGridLine) {
            // x 轴上网格线
            if (xDatas.size() > 0) {
                for (int i = 1; i < xDatas.size(); i++) {
                    int tempX = originPointX + i * xItemDistance;
                    canvas.drawLine(tempX, originPointY, tempX, yTopPoint + mTextSize, mPaint);
                }
            }
        }


        if (chartConfig.isNeedXY) {
            // y 轴
            canvas.drawLine(originPointX, originPointY, originPointX, yTopPoint, mPaint);
            // y轴最上边的箭头
            canvas.drawLine(originPointX, yTopPoint, originPointX - dp2px(3f), yTopPoint + dp2px(6f), mPaint);
            canvas.drawLine(originPointX, yTopPoint, originPointX + dp2px(3f), yTopPoint + dp2px(6f), mPaint);
        }

        if (chartConfig.isNeedGridLine && chartConfig.isShowYGridLine) {
            // y 轴上网格线
            if (yDatas.size() > 0) {
                for (int i = 1; i < yDatas.size(); i++) {
                    int tempY = originPointY - i * yItemDistance;
                    canvas.drawLine(originPointX, tempY, mWidth - originPointX, tempY, mPaint);
                }
            }
        }

    }

    private int dp2px(float dpValue) {
        final float scale = BaseApp.globalContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();
        }
    }

    private void setPathAnimation(Canvas canvas, Path path, int progress, Paint paint) {
        //  创建 PathMeasure 对象，并设置路径
        PathMeasure pathMeasure = new PathMeasure(path, false);
        //  通过 fraction 计算路径的长度
        float pathLength = pathMeasure.getLength() * progress / 100f;
        //  创建一个新路径，用于存储路径的一部分
        Path drawingPath = new Path();
        pathMeasure.getSegment(0, pathLength, drawingPath, true);
        //  绘制路径
        canvas.drawPath(drawingPath, paint);
    }
}
