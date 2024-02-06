package com.dpdp.testapplication.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dpdp.testapplication.activityutil.ActivityPluginUtil;

public class BaseDialog extends Dialog {

    private final Handler handler = new Handler(Looper.getMainLooper());

    public BaseDialog(@NonNull Context context, Builder builder) {
        super(context);
        initDialog(builder);
    }

    public BaseDialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context, themeResId);
        initDialog(builder);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Builder builder) {
        super(context, cancelable, cancelListener);
        initDialog(builder);
    }

    @NonNull
    protected View getContentView(){
        return contentView;
    }

    private View contentView;

    private void initDialog(Builder builder) {
        if (builder.layoutResId == 0) {
            throw new RuntimeException("the layoutResId not set yet");
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        contentView = LayoutInflater.from(getContext()).inflate(builder.layoutResId, null, false);
        setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = builder.dialogWidth == 0 ? layoutParams.width : builder.dialogWidth;
            layoutParams.height = builder.dialogHeight == 0 ? layoutParams.height : builder.dialogHeight;
            contentView.setLayoutParams(layoutParams);
        }
        if (builder.viewBindCallBack != null) {
            builder.viewBindCallBack.OnBindView(contentView, this);
        }
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);// 设置背景色，防止显示不全

            window.setGravity(builder.gravity);
            window.setDimAmount(builder.dimAmount);

            // 点击dialog外部阴影不会关闭dialog
            setCanceledOnTouchOutside(builder.outSideCanCancel);

            if (builder.outSideCanClick) {
                // 设置dialog外部事件透传可点击
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
            }
        }
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (builder.keyListener != null) {
                    return builder.keyListener.onKey(dialog, keyCode, event);
                }
                if (!builder.backCancelable) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
                return false;
            }
        });

        ActivityPluginUtil.addLifecycleListener(builder.context,new ActivityPluginUtil.LifecycleCallbackAdapter(){
            @Override
            public void onDestroy(Activity activity) {
                super.onDestroy(activity);
                // 防止某些情况activity 异常销毁 dialog没有清理掉
                Log.e("BaseDialog111", "Dialog: "+ toString() +" 依附的Activity： "+activity.toString()+" 销毁 页面上的Dialog自动 cancel---show---");
                cancel();
            }
        });
    }

    public interface ShowCallback {
        void showState(boolean success);
    }

    private ShowCallback showCallback;

    public void setShowResultCallback(ShowCallback callback) {
        this.showCallback = callback;
    }

    public void delayShow(long delayTimeMills) {
        handler.postDelayed(this::show, delayTimeMills);
    }

    public void delayDismiss(long delayTimeMills) {
        handler.postDelayed(this::cancel, delayTimeMills);
    }

    @Override
    public void show() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(this::show);
            return;
        }
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            if (activity.isFinishing() || activity.isDestroyed()) {
                Log.e("BaseDialog111", activity.toString() + "  activity.isFinishing() || activity.isDestroyed()");
                if (showCallback != null) {
                    showCallback.showState(false);
                }
                cancel();
                return;
            }
        }
        try {
            super.show();
            Log.e("BaseDialog111", "---show---"+toString());
            if (showCallback != null) {
                showCallback.showState(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BaseDialog111", e.toString());
            if (showCallback != null) {
                showCallback.showState(false);
            }
            cancel();
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            Log.e("BaseDialog111", "---dismiss---"+toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DialogManager.Companion.setShowingDialog(null);
        DialogManager.Companion.notifyDialogQueueDismiss();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    public static class Builder {

        protected Activity context;
        private float dimAmount = 0.5f;
        private int layoutResId;
        private boolean outSideCanCancel = true;
        private boolean backCancelable = true;
        private boolean outSideCanClick = false;
        private int dialogWidth;
        private int dialogHeight;
        private int gravity = Gravity.CENTER;
        protected int themeResId;
        private OnKeyListener keyListener;
        private IViewBindCallBack viewBindCallBack;

        public Builder(Activity context) {
            this.context = context;
        }

        public int getLayoutResId() {
            return layoutResId;
        }

        public Builder setDialogLayout(int layoutResId, IViewBindCallBack viewBindCallBack) {
            this.layoutResId = layoutResId;
            this.viewBindCallBack = viewBindCallBack;
            return this;
        }

        public int getDialogWidth() {
            return dialogWidth;
        }

        public Builder setDialogWidth(int dialogWidth) {
            this.dialogWidth = dialogWidth;
            return this;
        }

        public int getDialogHeight() {
            return dialogHeight;
        }

        public Builder setDialogHeight(int dialogHeight) {
            this.dialogHeight = dialogHeight;
            return this;
        }

        public float getDimAmount() {
            return dimAmount;
        }

        public Builder setDimAmount(float dimAmount) {
            this.dimAmount = dimAmount;
            return this;
        }

        public boolean isOutSideCanCancel() {
            return outSideCanCancel;
        }

        public Builder setOutSideCanCancel(boolean outSideCanCancel) {
            this.outSideCanCancel = outSideCanCancel;
            return this;
        }

        public boolean isBackCancelable() {
            return backCancelable;
        }

        public Builder setBackCancelable(boolean backCancelable) {
            this.backCancelable = backCancelable;
            return this;
        }

        public boolean isOutSideCanClick() {
            return outSideCanClick;
        }

        public Builder setOutSideCanClick(boolean outSideCanClick) {
            this.outSideCanClick = outSideCanClick;
            return this;
        }

        public int getGravity() {
            return gravity;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public int getThemeResId() {
            return themeResId;
        }

        public Builder setThemeResId(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public OnKeyListener getKeyListener() {
            return keyListener;
        }

        public Builder setKeyListener(OnKeyListener keyListener) {
            this.keyListener = keyListener;
            return this;
        }

        public IViewBindCallBack getViewBindCallBack() {
            return viewBindCallBack;
        }

        public BaseDialog build() {
            if (themeResId != 0)
                return new BaseDialog(context, themeResId, this);
            return new BaseDialog(context, this);
        }
    }

    public interface IViewBindCallBack {
        void OnBindView(View contentView, BaseDialog dialog);
    }
}
