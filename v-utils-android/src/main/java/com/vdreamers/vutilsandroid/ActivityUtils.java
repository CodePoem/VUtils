package com.vdreamers.vutilsandroid;

import android.app.Activity;

import java.util.Stack;

public class ActivityUtils {
    private static Stack<Activity> mActivityStack;
    private static volatile ActivityUtils mScreenManager;

    private ActivityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * Activity栈单例
     */
    public static ActivityUtils getInstance() {
        if (mScreenManager == null) {
            synchronized (ActivityUtils.class) {
                if (mScreenManager == null) {
                    mScreenManager = new ActivityUtils();
                }
            }
        }
        return mScreenManager;
    }

    /**
     * 移除栈顶activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            LogUtils.d("ActivityUtils pop:", activity.getLocalClassName());
        }
    }

    public Activity currentActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 将activity入栈
     *
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
        LogUtils.d("ActivityUtils push:", activity.getLocalClassName());
    }

    /**
     * 移除栈顶到cls之间的activity
     *
     * @param cls
     */
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            if (mActivityStack.empty()) {
                break;
            } else {
                Activity activity = mActivityStack.peek();
                if (activity == null) {
                    break;
                }
                if (activity.getClass().equals(cls)) {
                    break;
                }
                popActivity(activity);
                LogUtils.d("ActivityUtils pop:", activity.getLocalClassName());
            }
        }

    }

    /**
     * 打印栈内所有Activity名
     */
    public void logAllActivity() {
        for (Activity mActivity : mActivityStack) {
            LogUtils.d("ActivityUtils print:", mActivity.getLocalClassName());
        }
    }

    /**
     * 所有Activity出栈
     */
    public void popAllActivity() {
        while (true) {
            if (mActivityStack.empty()) {
                break;
            } else {
                Activity activity = mActivityStack.peek();
                if (activity == null) {
                    break;
                }
                popActivity(activity);
                LogUtils.d("ActivityUtils print:", activity.getLocalClassName());
            }
        }
    }
}
