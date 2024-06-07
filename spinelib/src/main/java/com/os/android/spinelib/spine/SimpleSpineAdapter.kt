package com.tcy365.m.hallhomemodule.view.spine

import com.badlogic.gdx.Files
import com.tcy365.m.hallhomemodule.view.spine.base.SpineBaseAdapter

/**
 * spine动画适配器
 * Date: 2024/6/5 11:00
 * Author: liangdp
 */
class SimpleSpineAdapter(
    val altasPath: String?,
    val skeletonPath: String?,
    val skin: String? = "",
    val animTag : String?,
    vararg val animationConfigs: SpineAnimConfig?
) : SpineBaseAdapter() {

    override fun onCreateImpl() {
        setAltasPath(altasPath ?: "", Files.FileType.Internal)
        setSkeletonPath(skeletonPath ?: "", Files.FileType.Internal)
        if (!skin.isNullOrEmpty())
            skinName = skin
        if (!animTag.isNullOrEmpty()){
            tag = animTag
        }
    }

    override fun onCreatedImpl() {
        setSkin(skinName)

        if (!animationConfigs.isNullOrEmpty())
            animationConfigs.forEach {
                it?.apply {
                    mAnimationState.addAnimation(trackIndex,animationName,isLoop,delay)
                }
            }
    }

    override fun doClick() {

    }
}