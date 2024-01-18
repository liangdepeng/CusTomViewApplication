package com.dpdp.testapplication.text;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.math.BigDecimal;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-15
 * <p>
 * Summary: TextView 跳动的动画 数字从 startValue 到 endValue 有一个数字不断跳动的过渡效果
 * 数字可以使任意类型的数字 可以是int long等等类型 传入时需转换为 String 类型
 */
public class NumberDanceTextView extends AppCompatTextView {

    private final String TAG = NumberDanceTextView.class.getSimpleName();
    private ValueAnimator animator;

    public NumberDanceTextView(@NonNull Context context) {
        super(context);
    }

    public NumberDanceTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberDanceTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * {@link #setNumberText(String, int)}
     */
    public void setNumberText(String targetNum) {
        this.setNumberText(targetNum, 3000);
    }

    /**
     * {@link #setNumberText(String, int, boolean, String,int)}
     */
    public void setNumberText(String targetNum, int duration) {
        this.setNumberText(targetNum, duration, false, "0",2);
    }

    /**
     * TextView 跳动的动画 数字从 startValue 到 endValue
     *
     * @param targetValue     结果数字
     * @param duration        动画时间
     * @param isHaveInitValue 是否设置初始值 如果 否 则取textView 当前的 数字为 初始值
     * @param startNum        如果有初始值设置初始值
     * @param scale           保留小数位数
     */
    public void setNumberText(String targetValue, int duration, boolean isHaveInitValue, String startNum,int scale) {
        if (animator != null && animator.isRunning()) {
            stopAnimation();
        }

        try {
            // 设置起始值
            String startValue = isHaveInitValue ? startNum : getText().toString();

            // 相同则取消更新;
            if (0 == compare(startValue, targetValue)) return;
            // 设置估值器
            DoubleEvaluator doubleEvaluator = new DoubleEvaluator();
            // 属性动画
            animator = ObjectAnimator.ofObject(doubleEvaluator, Double.parseDouble(startValue), Double.parseDouble(targetValue));
            // 差值器 线性变化
            animator.setInterpolator(new LinearInterpolator());
            // 持续时间
            animator.setDuration(duration);
            // 更新 UI
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    String value = new BigDecimal(String.valueOf(animation.getAnimatedValue()))
                            .setScale(scale, BigDecimal.ROUND_HALF_EVEN).toString();
                    setText(value);
                }
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    String value = new BigDecimal(targetValue)
                            .setScale(scale, BigDecimal.ROUND_HALF_EVEN).toString();
                    setText(value);
                }
            });
            animator.start();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            // 类型转换异常
            Toast.makeText(getContext(), "请输入正确格式的数字", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            // 异常则直接更新结果
            setText(String.valueOf(targetValue));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "---------------------onAttachedToWindow-----------------------");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "---------------------onDetachedFromWindow-----------------------");
        stopAnimation();
    }

    /**
     * 停止播放动画
     */
    private void stopAnimation() {
        if (animator != null) {
            animator.removeAllUpdateListeners();
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
    }

    /**
     * 自定义 估值器 控制变化的值
     */
    private static class DoubleEvaluator implements TypeEvaluator<Double> {

        /**
         * 计算中间估值
         *
         * @param fraction   动画的进度百分比
         * @param startValue 起始值
         * @param endValue   目标值
         * @return 估值
         */
        @Override
        public Double evaluate(float fraction, Double startValue, Double endValue) {
            return ((endValue - startValue) * fraction + startValue);
        }
    }

    /**
     * 比较两个数的大小
     *
     * @return {@code 1} if {@code this > val}, {@code -1} if {@code this < val},
     * {@code 0} if {@code this == val}.
     * @throws NullPointerException if {@code val == null}.
     */
    private int compare(String a, String b) {
        return new BigDecimal(a).compareTo(new BigDecimal(b));
    }
}
