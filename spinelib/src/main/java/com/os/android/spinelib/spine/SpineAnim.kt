package com.tcy365.m.hallhomemodule.view.spine

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.badlogic.gdx.utils.GdxNativesLoader
import com.tcy365.m.hallhomemodule.view.spine.base.OnSpineClickListener
import com.tcy365.m.hallhomemodule.view.spine.base.SpineBaseAdapter.OnSpineCreatedListener
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

/**
 * SpineAnim 简单封装
 *
 * 由于多个Spine动画对象需要依次创建，不可以同时创建多个
 * 所以多个动画串行队列执行
 *
 * Date: 2024/6/5 11:28
 * Author: liangdp
 */
class SpineAnim private constructor(builder: Builder?) : LifecycleEventObserver {

    companion object {
        init {
            GdxNativesLoader.load()
        }

       // var index = 0
        private val singleThreadPool = Executors.newSingleThreadExecutor()
        private val spineViewQueue = ConcurrentLinkedQueue<Builder>()
        private var isCreating = false
        private val handler = Handler(Looper.getMainLooper())
    }

    private var spineView: View? = null
    private var activity: Activity? = null
    private lateinit var spineAdapter: SimpleSpineAdapter

    init {
        spineViewQueue.add(builder)
        createNextSpineAnim()
    }

    private fun createNextSpineAnim() {
        if (isCreating)
            return
        // 确保按顺序执行
        singleThreadPool.execute {
            handler.post {
                if (isCreating)
                    return@post
                if (spineViewQueue.isEmpty())
                    return@post
                val builder = spineViewQueue.poll() ?: return@post
                isCreating = true
                //Log.e("spspspps","开始绘制第${index++}个动画  ${builder.tag}")
                createSpineAnim(builder)
            }
        }
    }

    fun showSpineAnimView(glSurfaceView: View?, container: ViewGroup?) {
        if (glSurfaceView == null || container == null)
            return
        container.post {
            try {
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(glSurfaceView, params)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSpineView(): View? {
        return spineView
    }

    fun release() {
        try {
            spineAdapter.dispose()
        } catch (e :Exception){
            e.printStackTrace()
        }
    }

    private fun createSpineAnim(builder: Builder?): View? {
        if (builder == null) return null
        activity = builder.activity
        val animConfigs = builder.animationConfigs
        spineAdapter = SimpleSpineAdapter(
            builder.altasPath, builder.skeletonPath,
            builder.skin, builder.tag, *animConfigs.toTypedArray()
        ).apply {
            setViewScale(builder.viewScaleX, builder.viewScaleY)
        }

        spineAdapter.setOnCreatedListener(object : OnSpineCreatedListener {
            override fun onCreated(tag: String) {
                handler.post {
                    isCreating = false
                    //Log.e("spspspps","结束绘制动画")
                    createNextSpineAnim()
                    builder.onSpineCreatedListener?.onCreated(tag)
                }
            }
        })

        if (builder.onSpineClickListener != null) {
            spineAdapter.setOnSpineClickListener(object :OnSpineClickListener{
                override fun onClick() {
                    handler.post {
                        builder.onSpineClickListener?.onClick()
                    }
                }
            })
        }

        spineView = spineAdapter.create(builder.activity, zOrderOnTop = builder.zOrderOnTop)

        if (builder.isDirectDisplay) {
            if (builder.delayMills > 0) {
                handler.postDelayed({
                    showSpineAnimView(spineView, builder.container)
                }, builder.delayMills)
            } else {
                showSpineAnimView(spineView, builder.container)
            }
        }

        return spineView
    }


    class Builder(val activity: Activity) {
        // 动画设置
        var altasPath: String? = null
            private set
        var skeletonPath: String? = null
            private set
        var skin: String? = null
            private set
        val animationConfigs: ArrayList<SpineAnimConfig> = ArrayList()
        var tag: String? = null
            private set

        // SpineView 动画容器
        var container: ViewGroup? = null
            private set
        var onSpineCreatedListener: OnSpineCreatedListener? = null
            private set
        var onSpineClickListener: OnSpineClickListener? = null
            private set

        // TODO 缩放比例 根据真实图尺寸和动画图尺寸计算，暂时没有找到动态计算的方法
        var viewScaleX: Float = 1f
            private set
        var viewScaleY: Float = 1f
            private set

        // 是否设置zOrderOnTop Z方向置顶
        var zOrderOnTop: Boolean = false
            private set

        // 是否直接显示  可手动控制显示时机
        var isDirectDisplay: Boolean = false
            private set

        // 延迟显示 仅在 isDirectDisplay = true 时生效
        var delayMills: Long = 0L
            private set

        fun setAltasPath(altasPath: String?): Builder {
            this.altasPath = altasPath
            return this
        }

        fun setSkeletonPath(skeletonPath: String?): Builder {
            this.skeletonPath = skeletonPath
            return this
        }

        fun setSkin(skin: String?): Builder {
            this.skin = skin
            return this
        }

        fun addAnimation(animConfig: SpineAnimConfig): Builder {
            animationConfigs.add(animConfig)
            return this
        }

        fun setTag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun setContainer(container: ViewGroup?): Builder {
            this.container = container
            return this
        }

        fun setOnSpineCreatedListener(onSpineCreatedListener: OnSpineCreatedListener?): Builder {
            this.onSpineCreatedListener = onSpineCreatedListener
            return this
        }

        fun setOnSpineClickListener(onSpineClickListener: OnSpineClickListener?): Builder {
            this.onSpineClickListener = onSpineClickListener
            return this
        }

        fun setDirectDisplay(directDisplay: Boolean): Builder {
            isDirectDisplay = directDisplay
            return this
        }

        fun setDelay(delayMills: Long): Builder {
            this.delayMills = delayMills
            return this
        }

        fun setViewScale(viewScaleX: Float, viewScaleY: Float): Builder {
            this.viewScaleX = viewScaleX
            this.viewScaleY = viewScaleY
            return this
        }

        fun setZOrderOnTop(zOrderOnTop: Boolean): Builder {
            this.zOrderOnTop = zOrderOnTop
            return this
        }

        fun create(): SpineAnim {
            return SpineAnim(this)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//        if (event==Lifecycle.Event.ON_PAUSE){
//
//        } else if (event==Lifecycle.Event.ON_RESUME){
//
//        }

    }
}
