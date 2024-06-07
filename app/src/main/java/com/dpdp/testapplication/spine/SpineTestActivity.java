package com.dpdp.testapplication.spine;

import android.widget.Toast;

import com.dpdp.testapplication.base.BaseVBActivity;
import com.dpdp.testapplication.databinding.ActivitySpineTestBinding;
import com.tcy365.m.hallhomemodule.view.spine.SpineAnim;
import com.tcy365.m.hallhomemodule.view.spine.SpineAnimConfig;


/**
 * Spine 动画测试
 */
public class SpineTestActivity extends BaseVBActivity<ActivitySpineTestBinding> {

    @Override
    public void initContentView() {
        new SpineAnim.Builder(this)
                .setAltasPath("testspine/matching.atlas")
                .setSkeletonPath("testspine/matching.json")
                .setContainer(mViewBinding.spineFl)
                .setTag("tasg")
                .setViewScale(2f,2f)
                .setDirectDisplay(true)
                .setZOrderOnTop(true)
                .addAnimation(new SpineAnimConfig(0,"loop",true,0f))
                .setOnSpineCreatedListener(tag -> {
                    // 动画创建完成
                })
                .setOnSpineClickListener(() -> {
                    Toast.makeText(this,"cat clicked!",Toast.LENGTH_SHORT).show();
                }).create();

        new SpineAnim.Builder(this)
                .setAltasPath("testspine/hero.atlas")
                .setSkeletonPath("testspine/hero.json")
                .setContainer(mViewBinding.spineFl2)
                .setViewScale(1f,1f)
                .setDirectDisplay(true)
                .setZOrderOnTop(true)
                .addAnimation(new SpineAnimConfig(0,"run",true,0f))
                .addAnimation(new SpineAnimConfig(0,"attack",true,5f))
                .addAnimation(new SpineAnimConfig(0,"walk",true,5f))
                .setOnSpineClickListener(() -> {
                    Toast.makeText(this,"tank clicked!",Toast.LENGTH_SHORT).show();
                }).create();



        new SpineAnim.Builder(this)
                .setAltasPath("testspine/stretchyman.atlas")
                .setSkeletonPath("testspine/stretchyman.json")
                .setContainer(mViewBinding.spineFl3)
                .setViewScale(1f,1f)
                .setDirectDisplay(true)
                .setZOrderOnTop(true)
                .addAnimation(new SpineAnimConfig(0,"sneak",true,0f))
                .setOnSpineCreatedListener(tag -> mViewBinding.scrollview.smoothScrollTo(0,0))
                .setOnSpineClickListener(() -> {
                    Toast.makeText(this,"tank clicked!",Toast.LENGTH_SHORT).show();
                }).create();

//        new SpineAnim.Builder(this)
//                .setAltasPath("testspine/tank.atlas")
//                .setSkeletonPath("testspine/tank.json")
//                .setContainer(mViewBinding.spineFl4)
//                .setViewScale(0.5f,0.5f)
//                .setDirectDisplay(true)
//                .setZOrderOnTop(true)
//                .addAnimation(new SpineAnimConfig(0,"shoot",true,0f))
//                .setOnSpineClickListener(() -> {
//                    Toast.makeText(this,"tank clicked!",Toast.LENGTH_SHORT).show();
//                }).create();
    }
}