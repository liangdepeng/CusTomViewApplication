package com.dpdp.testapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

import java.lang.reflect.ParameterizedType

/**
 *  实现 viewBinding 的 BaseActivity
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    lateinit var mViewBinding: VB

    override fun onCreate(onSavedInstanceState: Bundle?) {
        super.onCreate(onSavedInstanceState)
        val type = javaClass.genericSuperclass
        // 是否是一个泛型的实例
        if (type is ParameterizedType) {
            // 获取当前类的泛型参数类型 0 第一个 即 VB
            val clazz = type.actualTypeArguments[0] as Class<VB>
            // 反射获取 对应的方法调用 获取viewbinding
            val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
            mViewBinding = method.invoke(null, layoutInflater) as VB
            setContentView(mViewBinding.root)
            initContentView()
        }
    }

    abstract fun initContentView()
}