package com.dpdp.testapplication.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * 依附于Activity DecorView的popupView 局部阴影弹窗
 * 解决各种机型适配问题
 * <p>
 * Author: liangdp
 */
public class PartShadowPopup implements LifecycleEventObserver {

    private String TAG = "PartShadowPopup";
    private Builder builder;
    private FrameLayout rootLayout;
    private FrameLayout shadowLayout;
    private FrameLayout bgView;
    private View dialogView;
    private boolean isShowing = false;
    private boolean isAnimation = false;     // 是否正在动画中
    private final int[] anchorLoc = new int[2];
    private PopupViewCallback callback;
    private int animationDuration = 400;

    private PartShadowPopup(Builder builder) {
        this.builder = builder;
    }

    public void setCallback(PopupViewCallback callback) {
        this.callback = callback;
    }

    public void show() {
        // 正在进行动画或者正在展示中
        if (isAnimation || isShowing) {
            return;
        }
        if (!builder.isDismissDestroy() && dialogView != null && dialogView.getParent() != null) {
            startShow();
            return;
        }
        initPartShadowViewShow();
    }

    public void dismiss() {
        if (isAnimation || !isShowing || dialogView == null) {
            return;
        }
        dialogView.post(new Runnable() {
            @Override
            public void run() {
                isAnimation = true;
                ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f).setDuration(animationDuration);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (bgView != null) {
                            bgView.setAlpha(((float) animation.getAnimatedValue()));
                        }
                    }
                });
                animator.start();

                if (builder.getDirectionByAnchorView() == ANCHOR_VIEW_BOTTOM) {
                    dialogView.animate().translationY(-dialogView.getHeight())
                            .setDuration(animationDuration).setListener(dismissCallback).start();
                } else {
                    dialogView.animate().translationY(dialogView.getHeight())
                            .setDuration(animationDuration).setListener(dismissCallback).start();
                }
            }
        });
    }

    private final AnimatorListenerAdapter showCallback = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimation = false;
        }
    };

    private final AnimatorListenerAdapter dismissCallback = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            if (builder.isDismissDestroy()) {
                destroyView();
            } else if (rootLayout != null && rootLayout.getParent() != null) {
                rootLayout.setVisibility(View.GONE);
            }
            if (callback != null) {
                callback.onDismiss();
            }
            isShowing = false;
            isAnimation = false;
        }
    };


    private void initPartShadowViewShow() {
        final Activity activity = builder.getActivity();
        final ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getLifecycle().addObserver(this);
        }
        contentView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    View anchorView = builder.getAnchorView();
                    if (anchorView == null) return;
                    anchorView.getLocationInWindow(anchorLoc);

                    ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                    View activityContent = decorView.getChildAt(0);
                    ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(activityContent.getWidth(), activityContent.getHeight());
                    params.leftMargin = activityContent.getLeft();
                    params.topMargin = activityContent.getTop();
                    rootLayout = new FrameLayout(activity);
                    ((ViewGroup) activity.getWindow().getDecorView()).addView(rootLayout, params);

                    rootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (builder.isOutSideCanCancel()) {
                                dismiss();
                            }
                        }
                    });

                    ViewCompat.addOnUnhandledKeyEventListener(rootLayout, keyEventListener);

                    shadowLayout = new FrameLayout(activity);

                    FrameLayout.LayoutParams shadowlp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    if (builder.getDirectionByAnchorView() == ANCHOR_VIEW_BOTTOM) {
                        shadowlp.height = activityContent.getHeight() - anchorView.getHeight() - anchorLoc[1];
                        shadowlp.gravity = Gravity.BOTTOM;
                    } else {
                        shadowlp.height = anchorLoc[1];
                        shadowlp.gravity = Gravity.TOP;
                    }
                    rootLayout.addView(shadowLayout, shadowlp);

                    shadowLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (builder.isOutSideCanCancel()) {
                                dismiss();
                            }
                        }
                    });

                    bgView = new FrameLayout(activity);
                    bgView.setBackgroundColor(Color.argb((int) (255 * builder.getDimAmount()), 0, 0, 0));
                    bgView.setAlpha(0f);
                    shadowLayout.addView(bgView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

                    dialogView = LayoutInflater.from(activity).inflate(builder.getLayoutResId(), shadowLayout, false);

                    if (builder.getViewBindCallback() != null) {
                        builder.getViewBindCallback().onBindView(dialogView, PartShadowPopup.this);
                    }

                    final FrameLayout.LayoutParams dialoglp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    dialoglp.height = builder.getPopupHeight() != 0 ? builder.getPopupHeight() : FrameLayout.LayoutParams.WRAP_CONTENT;
                    dialoglp.width = builder.getPopupWidth() != 0 ? builder.getPopupWidth() : FrameLayout.LayoutParams.WRAP_CONTENT;
                    // TODO
                    // 宽度没撑满全屏的话还可以处理横向具体位置
                    if (builder.getDirectionByAnchorView() == ANCHOR_VIEW_BOTTOM) {
                        dialoglp.gravity = Gravity.TOP;
                    } else {
                        dialoglp.gravity = Gravity.BOTTOM;
                    }
                    if (builder.contentGravity == Gravity.LEFT || builder.contentGravity == Gravity.CENTER || builder.contentGravity == Gravity.RIGHT) {
                        dialoglp.gravity = dialoglp.gravity | builder.contentGravity;
                    }

                    shadowLayout.addView(dialogView, dialoglp);

                    dialogView.setVisibility(View.INVISIBLE);

                    dialogView.post(new Runnable() {
                        @Override
                        public void run() {

                            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(animationDuration);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    bgView.setAlpha(((float) animation.getAnimatedValue()));
                                }
                            });
                            animator.start();
                            isAnimation = true;

                            if (builder.getDirectionByAnchorView() == ANCHOR_VIEW_BOTTOM) {
                                dialogView.setTranslationY(-dialogView.getHeight());
                                dialogView.animate().translationY(0).setListener(showCallback).setDuration(animationDuration).start();
                            } else {
                                dialogView.setTranslationY(dialogView.getHeight());
                                dialogView.animate().translationY(0).setListener(showCallback).setDuration(animationDuration).start();
                            }
                            dialogView.setVisibility(View.VISIBLE);

                            isShowing = true;

                            if (callback != null) {
                                callback.onAfterShow(PartShadowPopup.this);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            destroyView();
        }
    }

    private void startShow() {
        dialogView.post(new Runnable() {
            @Override
            public void run() {
                isAnimation = true;
                rootLayout.setVisibility(View.VISIBLE);
                dialogView.setVisibility(View.INVISIBLE);

                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(animationDuration);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bgView.setAlpha(((float) animation.getAnimatedValue()));
                    }
                });
                animator.start();

                if (builder.getDirectionByAnchorView() == ANCHOR_VIEW_BOTTOM) {
                    dialogView.setTranslationY(-dialogView.getHeight());
                    dialogView.animate().translationY(0).setListener(showCallback).setDuration(animationDuration).start();
                } else {
                    dialogView.setTranslationY(dialogView.getHeight());
                    dialogView.animate().translationY(0).setListener(showCallback).setDuration(animationDuration).start();
                }
                dialogView.setVisibility(View.VISIBLE);

                isShowing = true;

                if (callback != null) {
                    callback.onAfterShow(PartShadowPopup.this);
                }
            }
        });
    }

    private void destroyView() {
        try {
            if (builder.mActivity instanceof FragmentActivity) {
                ((FragmentActivity) builder.mActivity).getLifecycle().removeObserver(this);
            }
            ViewCompat.removeOnUnhandledKeyEventListener(rootLayout, keyEventListener);
            shadowLayout.removeAllViews();
            rootLayout.removeAllViews();
            rootLayout.setVisibility(View.GONE);
            ((ViewGroup) builder.mActivity.getWindow().getDecorView()).removeView(rootLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final ViewCompat.OnUnhandledKeyEventListenerCompat keyEventListener = new ViewCompat.OnUnhandledKeyEventListenerCompat() {
        @Override
        public boolean onUnhandledKeyEvent(View v, KeyEvent event) {
            if (builder != null && builder.isBackCancelable() && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_UP && rootLayout != null) {
                dismiss();
                return true;
            }
            return false;
        }
    };

    public static final int ANCHOR_VIEW_TOP = 100;
    public static final int ANCHOR_VIEW_BOTTOM = 200;

    public static class Builder {

        private int layoutResId;
        // 点击外部是否可取消
        private boolean outSideCanCancel = true;
        // 返回键
        private boolean backCancelable = true;
        private View anchorView;
        //// 相对于 anchorView 显示方向  在某个view 上面或者下面
        private int showType = ANCHOR_VIEW_BOTTOM;
        // dialog内容相对于屏幕左右显示位置，Gravity.LEFT CENTER RIGHT
        private int contentGravity = Gravity.LEFT;
        private Activity mActivity;
        private float dimAmount = 0.5f;
        private IViewBindCallback viewBindCallback;
        private int popupHeight;
        private int popupWidth;

        // 关闭弹窗后是否保留示例，避免每次都生成新的，build之后保存对象即可复用
        private boolean dismissDestroy = true;


        public Builder(Activity context) {
            this.mActivity = context;
        }

        public Activity getActivity() {
            return mActivity;
        }

        public int getLayoutResId() {
            return layoutResId;
        }

        public int getPopupHeight() {
            return popupHeight;
        }

        public Builder setPopupHeight(int popupHeight) {
            this.popupHeight = popupHeight;
            return this;
        }

        public Builder setContentGravity(int contentGravity) {
            this.contentGravity = contentGravity;
            return this;
        }

        public int getPopupWidth() {
            return popupWidth;
        }

        public boolean isBackCancelable() {
            return backCancelable;
        }

        public Builder setBackCancelable(boolean backCancelable) {
            this.backCancelable = backCancelable;
            return this;
        }

        public Builder setPopupWidth(int popupWidth) {
            this.popupWidth = popupWidth;
            return this;
        }

        public Builder setLayoutResId(int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public IViewBindCallback getViewBindCallback() {
            return viewBindCallback;
        }

        public Builder setViewBindCallback(IViewBindCallback viewBindCallback) {
            this.viewBindCallback = viewBindCallback;
            return this;
        }

        public boolean isDismissDestroy() {
            return dismissDestroy;
        }

        public Builder setDismissDestroy(boolean dismissDestroy) {
            this.dismissDestroy = dismissDestroy;
            return this;
        }

        public int getDirectionByAnchorView() {
            return showType;
        }

        public Builder setDirectionByAnchorView(int showType) {
            this.showType = showType;
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


        public View getAnchorView() {
            return anchorView;
        }

        public Builder setAnchorView(View anchorView) {
            this.anchorView = anchorView;
            return this;
        }

        public PartShadowPopup build() {
            return new PartShadowPopup(this);
        }

    }

    public interface IViewBindCallback {
        void onBindView(View rootView, PartShadowPopup popupView);
    }

    public interface PopupViewCallback {
        void onAfterShow(PartShadowPopup popup);

        void onDismiss();
    }

}
