package com.dpdp.testapplication.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dpdp.testapplication.R;
import com.dpdp.testapplication.activityutil.ActivityPluginUtil;
import com.dpdp.testapplication.activityutil.ActivityStateEnum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-15
 * <p>
 * Summary: BaseActivity
 */
public class BaseActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    public final static ArrayList<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
        callbackLifecycle(ActivityStateEnum.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callbackLifecycle(ActivityStateEnum.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callbackLifecycle(ActivityStateEnum.ON_RESUME);
    }
    // 剩下的回调我就不多写了，以此类推

    @Override
    protected void onPause() {
        super.onPause();
        callbackLifecycle(ActivityStateEnum.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        callbackLifecycle(ActivityStateEnum.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        callbackLifecycle(ActivityStateEnum.ON_DESTROY);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    private void callbackLifecycle(ActivityStateEnum state) {
        // 查找Activity 是否有设置回调监控
        List<ActivityPluginUtil.LifecycleCallback> callbacks = ActivityPluginUtil.lifecycleListeners.get(this);
        if (callbacks != null && callbacks.size() > 0) {
            for (ActivityPluginUtil.LifecycleCallback callback : callbacks) {
                if (callback != null) {
                    // 反射获取方法名 直接调用对应的回调
                    Class<?> clazz = callback.getClass();
                    Method method = null;
                    try {
                        method = clazz.getDeclaredMethod(state.getMethodName(), Activity.class);
                        method.setAccessible(true);
                        method.invoke(callback, this);

                        Log.e("callbackLifecycle","反射调用成功->"+state.getMethodName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void showToast(CharSequence message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(() -> showToast(message));
            return;
        }
        try {
            Toast toast = new Toast(this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            View view = LayoutInflater.from(this).inflate(R.layout.common_toast_layout, null);
            ((TextView) view.findViewById(R.id.message)).setText(message);
            toast.setView(view);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Activity getTopActivity() {
        if (activities.isEmpty())
            return null;
        return activities.get(activities.size() - 1);
    }
}
