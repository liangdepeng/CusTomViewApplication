package com.dpdp.testapplication.image;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dpdp.testapplication.R;
import com.dpdp.testapplication.base.BaseVBActivity;
import com.dpdp.testapplication.databinding.ActivityImagetestBinding;

/**
 * xxxx
 * Date: 2024/6/28 10:55
 * Author: liangdp
 */
public class ImageActivity extends BaseVBActivity<ActivityImagetestBinding> {
    @Override
    public void initContentView() {
        mViewBinding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ImageActivity.this)
                        .load("https://activitynoticesysimage.tcy365.com/activitynoticeimage/2701be1d-4c9c-40ba-992d-46bffd213cf6.jpg")
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(mViewBinding.image1);
            }
        });

        mViewBinding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(ImageActivity.this)
                        .load("")
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                Log.e("image22", "onLoadFailed" + Thread.currentThread().toString());

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Glide.with(ImageActivity.this)
                                                .load(R.drawable.ic_launcher_background)
                                                .into(mViewBinding.image2);
                                    }
                                });


                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(mViewBinding.image2);
            }
        });
    }
}
