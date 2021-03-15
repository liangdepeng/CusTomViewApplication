package com.dpdp.testapplication.livelike;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.dpdp.testapplication.R;


/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-05
 * <p>
 * Summary: 点赞计数器 进度
 *
 * @see #firstClick() 可以用于 第一次 点击出现 不进行缩放 按照需要来
 * @see #release() 释放资源
 * @see #setLikeClickCallback(LikeClickCallback) 回调 点击次数
 */
public class LiveClickLikeView extends View implements View.OnClickListener {

    private int mWidth;// View的宽度
    private int mHeight;// View的高度
    private Paint bgPaint;// 背景画笔
    private RectF rect;// 圆环内切圆矩形
    private Paint bgArcPaint;// 圆环画笔
    private Paint progressArcPaint;// 进度画笔
    private Paint textPaint; // 中间文字画笔
    private int angle; // 绘制角度
    private int defaultSize = 200; // 默认一个最大尺寸
    private int clickCounts = 0;// 点击数
    private AnimatorSet animatorSet; // 动画集合
    private int bgPaintColor;// 圆形 背景色
    private int progressColor;// 进度条的颜色
    private int textColor; // 文字的颜色
    private int textSize;// 文字的 大小
    private int progressWidth; // 进度条宽度
    private ObjectAnimator animator; // 第一次显示出来大小不改变只有进度更新的动画

    public LiveClickLikeView(Context context) {
        this(context, null);
    }

    public LiveClickLikeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LiveClickLikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LiveClickLikeView);
            bgPaintColor = attributes.getColor(R.styleable.LiveClickLikeView_like_click_bg_circle_color, Color.BLACK);
            progressColor = attributes.getColor(R.styleable.LiveClickLikeView_like_click_progress_color, Color.BLUE);
            textColor = attributes.getColor(R.styleable.LiveClickLikeView_like_click_text_color, Color.WHITE);
            textSize = attributes.getDimensionPixelSize(R.styleable.LiveClickLikeView_like_click_text_size, 20);
            progressWidth = attributes.getDimensionPixelSize(R.styleable.LiveClickLikeView_like_click_progress_width, 10);
            attributes.recycle();
        }
        initPaint();
        setOnClickListener(this);
    }

    private void initPaint() {
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgPaintColor);
        bgPaint.setStyle(Paint.Style.FILL);

        rect = new RectF();

        bgArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgArcPaint.setColor(progressColor);
        bgArcPaint.setStyle(Paint.Style.STROKE);
        bgArcPaint.setStrokeWidth(progressWidth);

        progressArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressArcPaint.setColor(bgPaintColor);
        progressArcPaint.setStyle(Paint.Style.STROKE);
        progressArcPaint.setStrokeWidth(progressWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量宽高
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        // 获取宽高
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        // 根据测量的宽高 计算 圆环进度条的 内切圆的矩形的宽高
        rect.left = progressWidth >> 1;// 等价于 progressWidth/2
        rect.right = mWidth - (progressWidth >> 1);
        rect.top = progressWidth >> 1;
        rect.bottom = mHeight - (progressWidth >> 1);
    }

    private int measureSize(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            // //当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
            result = size;
        } else {
            result = defaultSize;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(defaultSize, size);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 背景
        canvas.drawCircle(mWidth >> 1, mHeight >> 1, mWidth >> 1, bgPaint);
        // 圆环
        canvas.drawArc(rect, -90, 360, false, bgArcPaint);
        // 叠加背景色一样的圆环 进度条消失动画的原理
        canvas.drawArc(rect, -90, angle, false, progressArcPaint);
        // 测量文字宽高
        float textWidth = textPaint.measureText("x " + clickCounts);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        //绘制文字  宽度超过 view宽度的 4/5，按0.8比例自动进行缩放
        while (textWidth > mWidth * 0.8f) {
            textSize = ((int) (textSize * 0.8f));
            textPaint.setTextSize(textSize);
            textWidth = textPaint.measureText("x " + clickCounts);
            textHeight = fontMetrics.bottom - fontMetrics.top;
        }
        canvas.drawText("x " + clickCounts, (mWidth - textWidth) / 2, (mHeight >> 1) + textHeight / 4, textPaint);
    }

    @Keep
    public void setAngle(int angle) {
        // 改变角度 不停地重绘 形成进度条效果
        this.angle = angle;
        invalidate();
    }

    // 区分用户频繁点击触发结束
    private boolean isUserCancel = false;

    @Override
    public void onClick(View v) {
        // 取消第一次的动画
        if (animator != null && animator.isRunning()) {
            animator.removeAllListeners();
            animator.cancel();
        }
        // 取消未完成的动画
        if (animatorSet != null && animatorSet.isRunning()) {
            isUserCancel = true;
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        //增加一次点击次数
        clickCounts++;
        // x缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.5f, 1f);
        // y缩放动画
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.5f, 1f);
        // 角度 动画 改变角度 形成进度条动画
        ObjectAnimator angle = ObjectAnimator.ofInt(this, "angle", 0, 360);
        // 进度条动画持续5秒
        angle.setDuration(5000);
        // 动画监听
        angle.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 如果是用户再次点击 取消上次动画，播放新的动画，上个动画结束不进行操作，
                // 只有动画自己执行完成才会回调 统计一次点赞次数
                if (isUserCancel) return;

                if (likeClickCallback != null) {
                    // 回调5秒内的点击次数
                    likeClickCallback.likeCountsCallback(clickCounts);
                    // 重置 点击次数
                    clickCounts = 0;
                }
            }
        });
        animatorSet = new AnimatorSet();
        // 组合动画
        animatorSet.playTogether(scaleX, scaleY, angle);
        animatorSet.setTarget(this);
        // 差值器
        animatorSet.setInterpolator(new AccelerateInterpolator());
        // 开始播放动画
        animatorSet.start();
        isUserCancel = false;
    }

    /**
     * 第一次点击的动画 只是进度条 不进行缩放
     */
    public void firstClick() {
        clickCounts++;
        animator = ObjectAnimator.ofInt(this, "angle", 0, 360);
        animator.setDuration(5000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isUserCancel) return;
                Log.e("animation", "1 auto end " + clickCounts);
                if (likeClickCallback != null) {
                    likeClickCallback.likeCountsCallback(clickCounts);
                    clickCounts = 0;
                }
            }
        });
        animator.start();
    }

    /**
     * 结束动画释放资源
     */
    public void release() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
            animatorSet = null;
        }
    }

    private LikeClickCallback likeClickCallback;

    public void setLikeClickCallback(LikeClickCallback likeClickCallback) {
        this.likeClickCallback = likeClickCallback;
    }

    public interface LikeClickCallback {
        void likeCountsCallback(int clickLikeCounts);
    }

}
