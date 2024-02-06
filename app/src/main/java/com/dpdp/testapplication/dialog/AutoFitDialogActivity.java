package com.dpdp.testapplication.dialog;

import android.view.View;

import com.dpdp.testapplication.R;
import com.dpdp.testapplication.base.BaseVBActivity;
import com.dpdp.testapplication.base.PxUtils;
import com.dpdp.testapplication.databinding.ActivityAutofixDialogLayoutBinding;
import com.dpdp.testapplication.databinding.DialogTipsBinding;

public class AutoFitDialogActivity extends BaseVBActivity<ActivityAutofixDialogLayoutBinding> {

    @Override
    public void initContentView() {

        mViewBinding.btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTestDialog(v).show();
            }
        });

        mViewBinding.btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTestDialog(v).show();

            }
        });

        mViewBinding.btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTestDialog(v).show();
            }
        });
    }


    public IntelligentDialog getTestDialog(View anchorView) {
        return new IntelligentDialog.QuickBuilder(this)
                .setDialogWidth(PxUtils.dp2px(260f))
                .setDimAmount(0.5f)
                .setDialogLayout(R.layout.dialog_tips, new BaseDialog.IViewBindCallBack() {
                    @Override
                    public void OnBindView(View contentView, BaseDialog dialog) {
                        DialogTipsBinding binding = DialogTipsBinding.bind(contentView);
                        binding.title.setText("警告");
                        binding.content.setText("这是消息的内容：\n一去二三里\n烟村四五家\n亭台六七座\n八九十枝花");
                        binding.btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setBackCancelable(true)
                .setAnchorView(anchorView)
                .build();
    }
}
