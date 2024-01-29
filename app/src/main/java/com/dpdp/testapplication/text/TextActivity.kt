package com.dpdp.testapplication.text

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import com.dpdp.testapplication.R
import com.dpdp.testapplication.base.BaseActivity
import com.dpdp.testapplication.databinding.AppActivityTextLayoutBinding
import java.util.ArrayList

/**
 * Created by ldp.
 *
 * Date: 2021-03-15
 *
 * Summary: 数字跳动动画、文字垂直跑马灯切换 测试
 */
class TextActivity : BaseActivity() {

    private val rootView by lazy { AppActivityTextLayoutBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView.root)
        initView()
    }

    private fun initView() {
        rootView.okBtn.setOnClickListener {
            startNumDance()
        }

        val list = ArrayList<String>()
        for (i in 0..9) { list.add("测试文字第${i}行") }
        rootView.mytextswicher.apply {
            setTextAppearance(R.style.my_textswitcher_style)
            setContentAutoLoop(list, 2500)
            setChildClickListener { content, index ->
                Toast.makeText(this@TextActivity, "$content 被点击 index-> $index", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startNumDance() {
        var startValue = rootView.startEt.text.toString().trim()
        val endValue = rootView.endEt.text.toString().trim()

        if (startValue.isEmpty()) {
            startValue = "0"
        }

        if (endValue.isEmpty()) {
            showToast("目标值不能为空~")
            return
        }

        if (TextUtils.equals(startValue, endValue)) {
            showToast("起始值和目标值相同~")
            return
        }

        try {
            rootView.numTextTv.setNumberText(endValue, 3000, true, startValue, 2)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            showToast("请输入正确格式的数字~")
        }

    }
}