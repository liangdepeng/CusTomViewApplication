package com.dpdp.testapplication.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dpdp.testapplication.base.ScreenUtils;

public class IntelligentDialog extends BaseDialog {

    @IntDef({Direction.VERTICAL, Direction.HORIZONTAL})
    public @interface Direction {
        int VERTICAL = 0;
        int HORIZONTAL = 1;
    }

    private IntelligentDialog(@NonNull Context context, QuickBuilder builder) {
        super(context, builder);
        init(builder);
    }

    private IntelligentDialog(@NonNull Context context, int themeResId, QuickBuilder builder) {
        super(context, themeResId, builder);
        init(builder);
    }

    private IntelligentDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, QuickBuilder builder) {
        super(context, cancelable, cancelListener, builder);
        init(builder);
    }

    private void init(QuickBuilder builder) {
        if (builder.anchorView == null)
            return;
        int screenHeight = ScreenUtils.getScreenHeight();
        int screenWidth = ScreenUtils.getScreenWidth();

        View contentView = getContentView();

        if (builder.getDialogWidth() == 0)
            throw new RuntimeException("please set dialogWidth!!!  invoke setDialogWidth(value>0)");

        contentView.measure(View.MeasureSpec.makeMeasureSpec(builder.getDialogWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

//             todo test code
//            contentView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e(IntelligentDialog.class.getSimpleName(),"实际宽高： dialogHeight-> "+contentView.getHeight()+"   dialogWidth->"+contentView.getWidth());
//                }
//            },2000);

        int dialogHeight = contentView.getMeasuredHeight();
        int dialogWidth = contentView.getMeasuredWidth();

        //   Log.e(IntelligentDialog.class.getSimpleName(), "测量宽高： dialogHeight-> " + dialogHeight + "   dialogWidth->" + dialogWidth);

        int[] locations = new int[2];
        builder.anchorView.getLocationOnScreen(locations);
        //     Log.e(IntelligentDialog.class.getSimpleName(), "location-> " + locations[0] + "   " + locations[1]);

        if (builder.getShowDirection() == Direction.VERTICAL) {

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            window.setGravity(Gravity.TOP);

            int y;
            if (locations[1] + dialogHeight > screenHeight) {
                y = locations[1] - dialogHeight - builder.anchorView.getHeight();
            } else {
                y = locations[1];
            }
            params.y = y;

            window.setAttributes(params);

        } else if (builder.getShowDirection() == Direction.HORIZONTAL) {

        }
    }


    public static class QuickBuilder extends Builder {

        @Direction
        private int showDirection = Direction.VERTICAL;

        private View anchorView;

        public QuickBuilder(Activity context) {
            super(context);
        }

        @Override
        public QuickBuilder setBackCancelable(boolean backCancelable) {
            return ((QuickBuilder) super.setBackCancelable(backCancelable));
        }

        @Override
        public QuickBuilder setOutSideCanCancel(boolean outSideCanCancel) {
            return (QuickBuilder) super.setOutSideCanCancel(outSideCanCancel);
        }

        @Override
        public QuickBuilder setOutSideCanClick(boolean outSideCanClick) {
            return (QuickBuilder) super.setOutSideCanClick(outSideCanClick);
        }

        @Override
        public QuickBuilder setDialogWidth(int dialogWidth) {
            return (QuickBuilder) super.setDialogWidth(dialogWidth);
        }

        @Override
        public QuickBuilder setDialogHeight(int dialogHeight) {
            return (QuickBuilder) super.setDialogHeight(dialogHeight);
        }

        @Override
        public QuickBuilder setDialogLayout(int layoutResId, IViewBindCallBack viewBindCallBack) {
            return (QuickBuilder) super.setDialogLayout(layoutResId, viewBindCallBack);
        }

        @Override
        public QuickBuilder setDimAmount(float dimAmount) {
            return (QuickBuilder) super.setDimAmount(dimAmount);
        }

        @Override
        public QuickBuilder setGravity(int gravity) {
            return (QuickBuilder) super.setGravity(gravity);
        }

        @Override
        public QuickBuilder setKeyListener(OnKeyListener keyListener) {
            return (QuickBuilder) super.setKeyListener(keyListener);
        }

        @Override
        public QuickBuilder setThemeResId(int themeResId) {
            return (QuickBuilder) super.setThemeResId(themeResId);
        }

        public int getShowDirection() {
            return showDirection;
        }


        public QuickBuilder setShowDirection(@Direction int showDirection) {
            this.showDirection = showDirection;
            return this;
        }

        public View getAnchorView() {
            return anchorView;
        }

        public QuickBuilder setAnchorView(View anchorView) {
            this.anchorView = anchorView;
            return this;
        }

        @Override
        public IntelligentDialog build() {
            if (themeResId != 0)
                return new IntelligentDialog(context, themeResId, this);
            return new IntelligentDialog(context, this);
        }
    }
}
