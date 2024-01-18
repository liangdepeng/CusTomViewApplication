package com.dpdp.testapplication.text;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.LineHeightSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 自定义view 形式 实现 NoPaddingTextView
 */
public class NoPaddingTextView extends AppCompatTextView {


    public NoPaddingTextView(@NonNull Context context) {
        this(context, null);
    }

    public NoPaddingTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoPaddingTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(getNoVerticalPaddingText(this,text), type);
    }

    // 设置上下取消绘制的padding值 考虑textsize
    public final SpannableString getNoVerticalPaddingText(TextView textView, CharSequence text) {
        if (textView == null || text == null)
            return new SpannableString("");
        // 如果原先上下有padding，重置为 0
        textView.setPadding(textView.getPaddingLeft(), 0, textView.getPaddingRight(), 0);
        // 利用 LineHeightSpan 快速实现去除 padding 的效果
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new LineHeightSpan() {
            @Override
            public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int lineHeight, Paint.FontMetricsInt fm) {
                Rect textRect = new Rect();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    textView.getPaint().getTextBounds(text, 0, text.length(), textRect);
                } else {
                    textView.getPaint().getTextBounds(text.toString(), 0, text.length(), textRect);
                }

                //Log.e("NoPaddingText", "修改之前 " + fm.toString());
                //Log.e("NoPaddingText", textRect.toString());
                //Log.e("NoPaddingText", "textSize: " + textView.getTextSize());

                if (textRect.bottom - textRect.top < textView.getTextSize()) {
                    // 一般我们认为字体的textview的textsize为textview的高度，当然有一定的误差 当然也可以自定义View 形式
                    // 当字体的高度没有字体的textsize大时，我们把大小设置成textsize，这样就可以解决文字的排版问题了
                    float tempPadding = (textView.getTextSize() - (textRect.bottom - textRect.top)) / 2f;
                    fm.top = (int) (textRect.top - tempPadding);
                    fm.bottom = (int) (textRect.bottom + tempPadding);
                } else {
                    // 这么设置可以完全消除padding，但是会有问题，
                    // 同样textsize的Textview 会因为设置的内容不一样而高度不一样
                    // 如果有什么特殊需求可以考虑用一下
                    fm.top = textRect.top;
                    fm.bottom = textRect.bottom;
                }
                fm.ascent = fm.top;
                fm.descent = fm.bottom;
                //Log.e("NoPaddingText", "修改之后 " + fm.toString());
            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
