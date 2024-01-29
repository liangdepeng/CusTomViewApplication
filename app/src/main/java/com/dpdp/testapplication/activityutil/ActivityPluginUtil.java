package com.dpdp.testapplication.activityutil;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 监控 Activity 生命周期
 */
public class ActivityPluginUtil {

    public static final HashMap<Activity, List<LifecycleCallback>> lifecycleListeners = new HashMap<>();

    public static void addLifecycleListener(Activity activity, LifecycleCallbackAdapter callbackAdapter) {
        if (activity == null || callbackAdapter == null)
            return;
        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        callbackAdapter.onCreate(activity);
                    } else if (event == Lifecycle.Event.ON_START) {
                        callbackAdapter.onStart(activity);
                    } else if (event == Lifecycle.Event.ON_RESUME) {
                        callbackAdapter.onResume(activity);
                    } else if (event == Lifecycle.Event.ON_PAUSE) {
                        callbackAdapter.onPause(activity);
                    } else if (event == Lifecycle.Event.ON_STOP) {
                        callbackAdapter.onStop(activity);
                    } else if (event == Lifecycle.Event.ON_DESTROY) {
                        callbackAdapter.onDestroy(activity);
                    }
                }
            });
        } else {
            List<LifecycleCallback> callbacks = lifecycleListeners.get(activity);
            if (callbacks == null) {
                ArrayList<LifecycleCallback> calllist = new ArrayList<>();
                calllist.add(callbackAdapter);
                lifecycleListeners.put(activity, calllist);
            } else {
                callbacks.add(callbackAdapter);
            }
        }
    }

    public static class LifecycleCallbackAdapter implements LifecycleCallback {
        @Override
        public void onCreate(Activity activity) { }

        @Override
        public void onStart(Activity activity) { }

        @Override
        public void onResume(Activity activity) { }

        @Override
        public void onPause(Activity activity) { }

        @Override
        public void onStop(Activity activity) { }

        @Override
        public void onDestroy(Activity activity) { }
    }

    public interface LifecycleCallback {
        void onCreate(Activity activity);
        void onStart(Activity activity);
        void onResume(Activity activity);
        void onPause(Activity activity);
        void onStop(Activity activity);
        void onDestroy(Activity activity);
    }
}
