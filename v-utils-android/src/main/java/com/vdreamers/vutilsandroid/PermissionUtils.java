package com.vdreamers.vutilsandroid;


import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 权限工具类
 * <p>
 * date 2019/03/15 01:10:26
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class PermissionUtils {

    /**
     * 权限工具类请求码
     */
    public static final int PERMISSION_UTILS_REQUEST_CODE = 8069;
    /**
     * 权限工具类标签
     */
    private static final String TAG = "PermissionUtils";

    private PermissionUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查是否拥有指定的所有权限
     *
     * @param mContext    调用方上下文
     * @param permissions 申请权限列表集合 {@link android.Manifest.permission}
     * @return 是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context mContext, List<String> permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int checkSelf = ContextCompat.checkSelfPermission(mContext.getApplicationContext
                        (), permission);
                if (checkSelf != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查是否拥有指定的所有权限
     *
     * @param mContext          调用方上下文
     * @param normalPermissions 正常申请权限列表集合 {@link android.Manifest.permission}
     * @param appOpsPermissions 可能需要适配第三方的权限列表集合 {@link android.Manifest.permission}
     * @return 是否拥有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Context mContext, List<String>
            normalPermissions, List<String> appOpsPermissions) {
        boolean checkSelfResult = true;
        boolean checkOpResult = true;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : normalPermissions) {
                int checkSelf = ContextCompat.checkSelfPermission(mContext.getApplicationContext
                        (), permission);
                if (checkSelf != PackageManager.PERMISSION_GRANTED) {
                    checkSelfResult = false;
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String appOpsPermission : appOpsPermissions) {
                //安卓原生权限管理 适配小米等第三方
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getApplicationContext()
                        .getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(appOpsPermission, Process.myUid(), mContext
                        .getApplicationContext()
                        .getPackageName());
                switch (checkOp) {
                    case AppOpsManager.MODE_ALLOWED:
                        LogUtils.d(TAG, "权限:允许");
                        checkOpResult = true;
                        break;
                    case AppOpsManager.MODE_IGNORED:
                        LogUtils.d(TAG, "权限:拒绝");
                        checkOpResult = false;
                        break;
                    case AppOpsManager.MODE_ERRORED:
                        LogUtils.d(TAG, "权限:错误");
                        checkOpResult = false;
                        break;
                    case 4:
                        LogUtils.d(TAG, "权限:询问");
                        checkOpResult = false;
                        break;
                    default:
                        checkOpResult = false;
                        break;
                }
            }
        }
        return (checkSelfResult && checkOpResult);
    }

    /**
     * 检查是否拥有指定的可能需要适配第三方所有权限
     *
     * @param mContext          调用方上下文
     * @param appOpsPermissions 可能需要适配第三方的权限列表集合 {@link android.Manifest.permission}
     * @return 是否拥有指定的可能需要适配第三方所有权限
     */
    public static boolean checkPermissionAppOpsGranted(Context mContext,
                                                       List<String> appOpsPermissions) {
        boolean checkOpResult = true;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String appOpsPermission : appOpsPermissions) {
                //安卓原生权限管理 适配小米等第三方
                AppOpsManager appOpsManager = (AppOpsManager) mContext.getApplicationContext()
                        .getSystemService(Context.APP_OPS_SERVICE);
                int checkOp = appOpsManager.checkOp(appOpsPermission, Process.myUid(), mContext
                        .getApplicationContext()
                        .getPackageName());
                switch (checkOp) {
                    case AppOpsManager.MODE_ALLOWED:
                        LogUtils.d(TAG, "权限:允许");
                        checkOpResult = true;
                        break;
                    case AppOpsManager.MODE_IGNORED:
                        LogUtils.d(TAG, "权限:拒绝");
                        checkOpResult = false;
                        break;
                    case AppOpsManager.MODE_ERRORED:
                        LogUtils.d(TAG, "权限:错误");
                        checkOpResult = false;
                        break;
                    case 4:
                        LogUtils.d(TAG, "权限:询问");
                        checkOpResult = false;
                        break;
                    default:
                        checkOpResult = false;
                        break;
                }
            }
        }
        return checkOpResult;
    }


    /**
     * 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
     *
     * @param activity    调用方Activity
     * @param permissions 申请权限列表集合 {@link android.Manifest.permission}
     */
    public static void requestPermission(Activity activity, List<String> permissions) {
        int normalPermissionsNumber = permissions.size();
        ActivityCompat.requestPermissions(activity, permissions.toArray(new
                String[normalPermissionsNumber]), PERMISSION_UTILS_REQUEST_CODE);
    }

    /**
     * 获取是否应该展示权限解释说明UI
     * <p>
     * 之前没有拒绝过此权限的申请（第一次安装后请求权限前调用）：false
     * 曾经被拒绝过权限后再调用：true
     * 曾经被拒绝过权限且不再询问后再调用：false
     * 系统不允许任何程序获取该权限：false
     * Android 6.0以下返回：false
     * 总是允许权限后再次调用：false
     *
     * @param activity   调用方Activity
     * @param permission 申请权限 {@link android.Manifest.permission}
     * @return 是否应该展示权限解释说明UI
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity,
                                                               String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
