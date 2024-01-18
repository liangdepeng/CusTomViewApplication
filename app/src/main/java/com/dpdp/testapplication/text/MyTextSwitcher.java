package com.dpdp.testapplication.text;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import com.dpdp.testapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现TextView切换动画效果
 * 默认上下滚动 其他效果可以自定义
 * 文字样式也可以自定义
 */
public class MyTextSwitcher extends TextSwitcher {

    // 需要切换的内容列表
    private final ArrayList<String> contentList = new ArrayList<>();
    // 切换的时间间隔
    private long switchTimeMills;
    // 点击事件
    private OnClickListener clickListener;


    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 233) {
                MyTextSwitcher.this.setText(getShowText());
                msg.getTarget().sendEmptyMessageDelayed(233, switchTimeMills);
            }
        }
    };
    private int contentIndex;

    private CharSequence getShowText() {
        if (++contentIndex >= contentList.size()) {
            contentIndex = 0;
        }
        return contentList.get(contentIndex);
    }

    public MyTextSwitcher(Context context) {
        this(context, null);
    }

    public MyTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 设置进入动画
        setInAnimation(getContext(), R.anim.text_inanim);
        // 设置退出动画
        setOutAnimation(getContext(), R.anim.text_outanim);
        // 产生两个Textview 在这里可以自定义
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(getContext());
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);
                return textView;
            }
        });
    }

    /**
     * @param list            循环播放的内容
     * @param switchTimeMills 切换间隔
     */
    public void setContentAutoLoop(List<String> list, long switchTimeMills) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.switchTimeMills = switchTimeMills;
        contentList.clear();
        contentList.addAll(list);
        contentIndex = 0;
        // 设置第一个显示内容
        setCurrentText(contentList.get(contentIndex));
        if (contentList.size() == 1) {
            return;
        }
        // handler 开始循环
        handler.sendEmptyMessageDelayed(233, switchTimeMills);
    }

    /**
     * 设置点击事件
     */
    public void setChildClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
        initClickListener();
    }


    /**
     * 设置 切换的动画样式 默认是上下滚动切换，这里动画根据自己想要的效果切换，任意动画效果都可以
     *
     * @param inAnimId  进入的动画
     * @param outAnimId 退出的动画
     */
    public void setAnimation(@AnimRes int inAnimId, @AnimRes int outAnimId) {
        setInAnimation(getContext(), inAnimId);
        setOutAnimation(getContext(), outAnimId);
    }

    /**
     * 设置文字属性
     *
     * @param styleId 属性配置文件
     */
    public void setTextAppearance(@StyleRes int styleId) {
        ((TextView) getCurrentView()).setTextAppearance(getContext(), styleId);
        ((TextView) getNextView()).setTextAppearance(getContext(), styleId);
    }


    private void initClickListener() {
        getCurrentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.OnItemClick(contentList.get(contentIndex), contentIndex);
                }
            }
        });
        getNextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.OnItemClick(contentList.get(contentIndex), contentIndex);
                }
            }
        });
    }

    /**
     * 获取 两个 子view textview
     * 如果想对他们做一些特殊处理的话可以使用该方法获取对象
     */
    public List<TextView> getViews() {
        ArrayList<TextView> textViews = new ArrayList<>();
        textViews.add(((TextView) getCurrentView()));
        textViews.add(((TextView) getNextView()));
        return textViews;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacksAndMessages(null);
    }

    public interface OnClickListener {
        void OnItemClick(String content, int index);
    }
}
