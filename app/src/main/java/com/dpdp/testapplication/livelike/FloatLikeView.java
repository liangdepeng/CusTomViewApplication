package com.dpdp.testapplication.livelike;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.dpdp.testapplication.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-02-23
 * <p>
 * Summary: 点赞爱心动画
 * <p>
 * 固定宽高
 * <p>
 * 包含一个子view 或者没有 要放在最下面 动效爱心往上飘
 * <p>
 *
 * @see #setLikeDrawables(int...) 设置 对应的 图片
 * @see #clickLikeView() 点击调用 开始动画 点一次出一个
 * @see #autoPlayClickView(int, boolean) 自动播放动画
 * @see #stopAutoPlay() 停止自动播放
 * @see #release() 释放资源 退出时调用
 */
public class FloatLikeView extends FrameLayout {

    private ArrayList<Drawable> mLikeDrawables;// 点赞的drawable集合
    private Random mRandom; //产生随机数来产生随机爱心
    private final Context context;
    private int mLikePicWidth = 0;// 爱心的 view 宽
    private int mLikePicHeight = 0;// 爱心的 view 高
    private LayoutParams mLikePicLayoutParams;// 布局参数
    private int mLikePicBottomMargin = 0;// 爱心出现位置距离底部
    private int mChildHeight = 0;// 如果底部设置了一个 view 爱心会在其上面
    private int mWidth;// 此布局view 宽
    private int mHeight;// 此布局view 高
    private AnimatorSet animator;// 爱心动画

    public FloatLikeView(@NonNull Context context) {
        this(context, null);
    }

    public FloatLikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLikeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        // 存放 要显示的 效果
        mLikeDrawables = new ArrayList<>();
        // 设置一个默认的爱心
        mLikeDrawables.add(ContextCompat.getDrawable(context, R.drawable.ui_default_heart));
        // 产生随机数 随机选择 出现的图形
        mRandom = new Random();
    }

    /**
     * 设置 动画飘动的资源文件
     */
    public void setLikeDrawables(int... drawableIds) {
        if (drawableIds != null && drawableIds.length > 0) {
            mLikeDrawables.clear();
            for (int drawableId : drawableIds) {
                mLikeDrawables.add(ContextCompat.getDrawable(context, drawableId));
            }
        }
    }

    private ValueAnimator valueAnimator;

    /**
     * 自动 播放 飘爱心 动画 利用属性动画 进行3秒的
     * <p>
     * 设置了最大 30 个 看情况自己设置好吧 太多了不好看
     *
     * @param likeCounts 飘心的数量
     * @param isRepeat   是否重复播放
     *                   停止播放   {@link #stopAutoPlay()}
     */
    public void autoPlayClickView(int likeCounts, boolean isRepeat) {
        // 重复调用 则取消上次的重新开始
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt(0, Math.min(likeCounts, 30));//30
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        // 是否 开启循环播放 -====
        valueAnimator.setRepeatCount(isRepeat ? ValueAnimator.INFINITE : 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int lastValue = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = ((int) animation.getAnimatedValue());
                // 利用 动画的更新回调 相当于一个定时效果 但是要注意 会有多次相同的回调 要判断一下
                if (animatedValue == lastValue) return;
                // 手动调用一次 相当于点击一次 出一个爱心效果
                clickLikeView();
                // 记录上次的值
                lastValue = animatedValue;
            }
        });
        valueAnimator.start();
    }

    /**
     * 是否有动画在播放中
     */
    public boolean isAnimationRunning() {
        if (animator != null) {
            return animator.isRunning();
        }
        if (valueAnimator != null) {
            return valueAnimator.isRunning();
        }
        return false;
    }

    /**
     * 停止自动播放
     * <p>
     * {@link #autoPlayClickView(int, boolean)} 自动播放
     */
    public void stopAutoPlay() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator.removeAllUpdateListeners();
        }
    }

    /**
     * 退出时 释放资源
     */
    public void release() {
        stopAutoPlay();
        if (animator != null) {
            animator.cancel();
            animator.removeAllListeners();
        }
    }

    /**
     * 点击调用 产生动效 调用一次 产生一个爱心飘动效果
     */
    public void clickLikeView() {

        if (mLikePicWidth == 0 || mLikePicHeight == 0 || mLikePicLayoutParams == null) {
            // 获取 点赞的 view 尺寸 ，这边定一个 统一尺寸
            mLikePicWidth = mLikeDrawables.get(0).getIntrinsicWidth();
            mLikePicHeight = mLikeDrawables.get(0).getIntrinsicHeight();

            // 指定爱心出现的位置
            mLikePicLayoutParams = new LayoutParams(mLikePicWidth, mLikePicHeight);
            // 位置在底部的中间
            mLikePicLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            // 如果底部有一个 childView 则会在其上方 飘爱心
            mLikePicLayoutParams.bottomMargin = mLikePicBottomMargin;
        }

        ImageView likeIv = new ImageView(context);
        likeIv.setImageDrawable(mLikeDrawables.get(mRandom.nextInt(mLikeDrawables.size())));
        likeIv.setLayoutParams(mLikePicLayoutParams);
        addView(likeIv);
        addAnimationStart(likeIv);
    }


    /**
     * 飘爱心动画 主要逻辑
     */
    private void addAnimationStart(ImageView likeIv) {
        //----------------------------------- 出现 的动画-----------------------------------
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(likeIv, "alpha", 0.5f, 1f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(likeIv, "scaleX", 0.6f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(likeIv, "scaleY", 0.6f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(200);
        animatorSet.setTarget(likeIv);

        //------------------------ 路径移动动画  基于 三阶贝塞尔曲线-------------------------
        PathEvaluator pathEvaluator = new PathEvaluator(getControlPointF(1), getControlPointF(2));
        // 设置 起点
        PointF startPointF = new PointF((float) (mWidth - mLikePicWidth) / 2, mHeight - mLikePicBottomMargin - mLikePicHeight);
        // 设置 终点
        PointF endPointF = new PointF((float) mWidth / 2 + (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextInt(100), 0);
        // 自定义 属性动画 利用贝塞尔曲线 计算路径上的各个点的位置
        ValueAnimator valueAnimator = ValueAnimator.ofObject(pathEvaluator, startPointF, endPointF);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (likeIv == null) return;
                // 根据各个点的位置 改变 view 的位置
                PointF pointF = (PointF) animation.getAnimatedValue();
                likeIv.setX(pointF.x);
                likeIv.setY(pointF.y);
                // 根据 动画的进度 设置透明度
                likeIv.setAlpha(1f - animation.getAnimatedFraction());
            }
        });
        valueAnimator.setTarget(likeIv);

        animator = new AnimatorSet();
        animator.setTarget(likeIv);
        // 动画顺序播放
        animator.playSequentially(animatorSet, valueAnimator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 动画结束 移除添加的 view
                // ~!!!
                // ~!!!
                // ~!!!
                removeView(likeIv);
            }
        });
        animator.start();

    }

    /**
     * 随机产生 三阶贝赛尔曲线 中间两个控制点
     *
     * @param value 控制点
     */
    private PointF getControlPointF(int value) {
        PointF pointF = new PointF();
        pointF.x = (float) mWidth / 2 - mRandom.nextInt(100);
        pointF.y = mRandom.nextInt((mHeight - mLikePicBottomMargin - mLikePicHeight) / value);
        return pointF;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        // 包含一个子view 或者没有 要放在最下面
        if (mChildHeight == 0 && childCount > 0) {
            View child = getChildAt(0);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            mChildHeight = child.getMeasuredHeight();
            mLikePicBottomMargin = mChildHeight;
        }

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    private static class PathEvaluator implements TypeEvaluator<PointF> {

        private final PointF point01;
        private final PointF point02;

        public PathEvaluator(PointF point01, PointF point02) {
            this.point01 = point01;
            this.point02 = point02;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

            float change = 1.0f - fraction;
            PointF pointF = new PointF();

            // 三阶贝塞儿曲线
            pointF.x = (float) Math.pow(change, 3) * startValue.x
                    + 3 * (float) Math.pow(change, 2) * fraction * point01.x
                    + 3 * change * (float) Math.pow(fraction, 2) * point02.x
                    + (float) Math.pow(fraction, 3) * endValue.x;
            pointF.y = (float) Math.pow(change, 3) * startValue.y
                    + 3 * (float) Math.pow(change, 2) * fraction * point01.y
                    + 3 * change * fraction * fraction * point02.y
                    + (float) Math.pow(fraction, 3) * endValue.y;

            return pointF;
        }
    }


}
