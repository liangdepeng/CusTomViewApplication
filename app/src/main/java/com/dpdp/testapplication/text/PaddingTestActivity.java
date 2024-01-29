package com.dpdp.testapplication.text;

import com.dpdp.testapplication.base.BaseVBActivity;
import com.dpdp.testapplication.databinding.ActivityPaddingTestBinding;

/**
 * NoPadding Textview 测试
 */
public class PaddingTestActivity extends BaseVBActivity<ActivityPaddingTestBinding> {

    @Override
    public void initContentView() {
        TextUtil.setNoVerticalPaddingText(mViewBinding.testTv, mViewBinding.testTv.getText());

        // 设置上下取消绘制的padding值 考虑 TextSize
        TextUtil.setNoVerticalPaddingText(mViewBinding.noPaddingTv, "一二三四五六七八九十");
        TextUtil.setNoVerticalPaddingText(mViewBinding.noTv1, mViewBinding.noTv1.getText());
        TextUtil.setNoVerticalPaddingText(mViewBinding.noTv2, mViewBinding.noTv2.getText());
        TextUtil.setNoVerticalPaddingText(mViewBinding.noTv3, mViewBinding.noTv3.getText());
        TextUtil.setNoVerticalPaddingText(mViewBinding.noTv4, mViewBinding.noTv4.getText());

        // 设置上下取消绘制的padding值  不考虑 TextSize
        TextUtil.setNoVerticalPaddingText2(mViewBinding.errorTv1, mViewBinding.errorTv1.getText());
        TextUtil.setNoVerticalPaddingText2(mViewBinding.errorTv2, mViewBinding.errorTv2.getText());
        TextUtil.setNoVerticalPaddingText2(mViewBinding.errorTv3, mViewBinding.errorTv3.getText());
        TextUtil.setNoVerticalPaddingText2(mViewBinding.errorTv4, mViewBinding.errorTv4.getText());
        TextUtil.setNoVerticalPaddingText2(mViewBinding.errorTv5, mViewBinding.errorTv5.getText());
    }
}
