package com.dpdp.testapplication.livelike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.dpdp.testapplication.base.BaseActivity
import com.dpdp.testapplication.R
import com.dpdp.testapplication.databinding.AppActivityLiveLikeTestLayoutBinding

/**
 * Created by ldp.
 *
 * Date: 2021-03-15
 *
 * Summary:点赞/计时器 效果测试
 */
class LiveLikeTestActivity : BaseActivity() {

    // ViewBinding bind View
    private val rootView by lazy { AppActivityLiveLikeTestLayoutBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // rootView
        setContentView(rootView.root)
        initView()
    }

    private fun initView() {
        // 添加图片
        rootView.likeBtn.setLikeDrawables(R.drawable.like_gift_ic, R.drawable.like_heart_blue_ic, R.drawable.like_heart_pink_ic,
                R.drawable.like_heart_purple_ic, R.drawable.like_heart_red_ic, R.drawable.like_star_blue_ic,
                R.drawable.like_star_pink_ic, R.drawable.like_star_yellow_ic)
        rootView.praiseSingleBtn.setOnClickListener {
            // 单次点击 出一个爱心
            rootView.likeBtn.clickLikeView()
        }

        rootView.praiseMultiBtn.setOnClickListener {
            if (rootView.likeBtn.isAnimationRunning) {
                rootView.likeBtn.stopAutoPlay()
                rootView.likeBtn.release()
                showToast("动画已停止")
                return@setOnClickListener
            }
            // 点击爱心动画自动播放
            rootView.likeBtn.autoPlayClickView(20, true)
        }

        rootView.calculateNumBtn.setOnClickListener {
            //计时器
            if (rootView.likeCountV.visibility == View.VISIBLE)
                return@setOnClickListener
            rootView.likeCountV.visibility = View.VISIBLE
            rootView.likeCountV.firstClick()
        }
        rootView.likeCountV.setLikeClickCallback(object : LiveClickLikeView.LikeClickCallback {
            override fun likeCountsCallback(clickLikeCounts: Int) {
                showToast("点击次数： $clickLikeCounts 次")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 结束动画释放资源
        rootView.likeBtn.release()
        rootView.likeCountV.release()
    }
}