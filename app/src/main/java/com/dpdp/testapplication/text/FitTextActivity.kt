package com.dpdp.testapplication.text

import android.widget.Toast
import com.dpdp.testapplication.base.BaseVBActivity
import com.dpdp.testapplication.databinding.ActivityAutofitLayoutBinding

class FitTextActivity : BaseVBActivity<ActivityAutofitLayoutBinding>() {

    private var showStr = "我是一段测试的文字test"

    override fun initContentView() {
        mViewBinding.fitTv.text = showStr

        mViewBinding.sizeBtn.setOnClickListener {
            val size = mViewBinding.edit.text.toString().trim().toFloat()
            mViewBinding.fitTv.textSize = size
            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show()
        }

        mViewBinding.edit.setText("${(mViewBinding.fitTv.textSize/resources.displayMetrics.scaledDensity)}")

        mViewBinding.addBtn.setOnClickListener {
            showStr = "${showStr}测试"
            mViewBinding.fitTv.text = showStr
        }

        mViewBinding.subBtn.setOnClickListener {
            if (showStr.isEmpty()){
                showStr = ""
                return@setOnClickListener
            }
            showStr = showStr.substring(0,showStr.length-1)
            mViewBinding.fitTv.text = showStr
        }
    }
}