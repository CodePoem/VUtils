package com.codepoem.vutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * @author dyb
 * @createTime 2017/9/11 13:42
 */

public class ScreenUtils {

    private ScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取屏幕宽度
     *
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        int screenWidth = 0;
        try {
            DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
            screenWidth = dm.widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    /**
     * 获取屏幕尺高度
     *
     * @param mContext
     * @return
     */
    public static int getScreenHeight(Context mContext) {
        int screenHeight = 0;
        try {
            DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
            screenHeight = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    /**
     * .获取当前设备状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getApplicationContext()
                .getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getApplicationContext()
                    .getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }
}
