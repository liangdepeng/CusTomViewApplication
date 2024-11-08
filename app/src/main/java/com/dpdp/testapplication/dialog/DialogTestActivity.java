package com.dpdp.testapplication.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dpdp.testapplication.R;
import com.dpdp.testapplication.activityutil.ActivityPluginUtil;
import com.dpdp.testapplication.base.BaseVBActivity;
import com.dpdp.testapplication.databinding.ActivityDialogTestBinding;

public class DialogTestActivity extends BaseVBActivity<ActivityDialogTestBinding> implements DialogTestAdapter.OnItemClickListener {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle onSavedInstanceState) {
        ActivityPluginUtil.addLifecycleListener(this,new ActivityPluginUtil.LifecycleCallbackAdapter());
        super.onCreate(onSavedInstanceState);
    }

    @Override
    public void initContentView() {
        mViewBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        DialogTestAdapter testAdapter = new DialogTestAdapter(this);
        testAdapter.setItemClickListener(this);
        mViewBinding.recyclerview.setAdapter(testAdapter);
        testAdapter.refreshData();
    }

    @Override
    public void OnItemClick(String itemShowContent, int position) {
        if (DialogTestAdapter.LOADING.equals(itemShowContent)){
            DialogManager.Companion.showLoadingDialog("");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogManager.Companion.dismissLoadingDialog();
                }
            }, 5000);
        }else if (DialogTestAdapter.TIPS.equals(itemShowContent)){
            DialogManager.Companion.showTipsDialog(DialogTestActivity.this,"标题一","内容一测试测试测试测试试吃内容",true);
            DialogManager.Companion.showTipsDialog(DialogTestActivity.this,"标题二","内容一哈哈哈哈哈哈哈哈哈",true);
            DialogManager.Companion.showTipsDialog(DialogTestActivity.this,"标题三","内容一嘎嘎嘎嘎嘎嘎嘎嘎",true);
            DialogManager.Companion.showTipsDialog(DialogTestActivity.this,"标题四","内容一呵呵呵呵呵呵呵",true);
            DialogManager.Companion.showTipsDialog(DialogTestActivity.this,"标题五","内容一黑hi额hi额hi额hi额hi额诶和hi",true);
        }else if (DialogTestAdapter.AUTO_FIX.equals(itemShowContent)){
            startActivity(new Intent(this, AutoFitDialogActivity.class));
        }else if (DialogTestAdapter.PART_SHADOW.equals(itemShowContent)){
            new PartShadowPopup.Builder(this)
                    .setPopupWidth((int) (getResources().getDisplayMetrics().widthPixels/2f))
                    .setAnchorView(mViewBinding.recyclerview)
                    .setOutSideCanCancel(true)
                    .setContentGravity(Gravity.LEFT)
                    .setDimAmount(0.6f)
                    .setDirectionByAnchorView(PartShadowPopup.ANCHOR_VIEW_BOTTOM)
                    .setLayoutResId(R.layout.dialog_popup_test)
                    .setViewBindCallback(new PartShadowPopup.IViewBindCallback() {
                        @Override
                        public void onBindView(View rootView, PartShadowPopup popupView) {
                            TextClock textclock = rootView.findViewById(R.id.textclock);
                            textclock.setFormat24Hour("kk:mm:ss");
                            textclock.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupView.dismiss();
                                }
                            });
                        }
                    }).build().show();
        }
    }
}
