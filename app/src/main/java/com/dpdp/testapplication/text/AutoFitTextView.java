package com.dpdp.testapplication.text;


import android.content.Context;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-15
 * <p>
 * Summary: 自适应大小 单行 TextView
 *
 */
public class AutoFitTextView extends AppCompatTextView {

    public AutoFitTextView(@NonNull Context context) {
        this(context, null);
    }

    public AutoFitTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMaxLines(1);
    }

    @Override
    protected void onTextChanged(final CharSequence text, int start, int lengthBefore, int lengthAfter) {
        // 确保 获取到 getWidth() 的值，尤其是在列表中
        post(new Runnable() {
            @Override
            public void run() {
                refitText(text.toString(),getTextMaxWidth()); // 重新设置文本大小
            }
        });
    }



    private void refitText(String text, int textWidth) {
        if (textWidth <= 0 || text == null || text.isEmpty()) {
            return;
        }
        float targetWidth = textWidth - getPaddingLeft() - getPaddingRight();
        Paint paint = getPaint();
        // fix 每次都获取最大可用尺寸来进行缩放 避免字体越来越小
        float textSize = getMaxSize();
        paint.setTextSize(textSize);
        //Log.e("AutoFitTextView",String.valueOf(textSize)+"  start");
        while (paint.measureText(text) > targetWidth && textSize > 1) {
            textSize--;
            paint.setTextSize(textSize);
           // Log.e("AutoFitTextView",String.valueOf(textSize)+"  inging");
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize); // 更新TextView的字体大小
       // Log.e("AutoFitTextView",String.valueOf(textSize)+"  end");
    }

    private float maxSize = 0f;

    private float getMaxSize() {
        maxSize = Math.max(maxSize,getTextSize());
        return maxSize;
    }

    private int maxTextWidth = 0;

    // 修正 布局未设置固定宽度一直变化的情况
    // 计算最大宽度 防止字体缩小后无法变大
    private int getTextMaxWidth() {
        maxTextWidth = Math.max(maxTextWidth,getWidth());
        return maxTextWidth;
    }
}








