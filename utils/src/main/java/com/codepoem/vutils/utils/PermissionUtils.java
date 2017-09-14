package com.codepoem.vutils.utils;


import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.List;

public class PermissionUtils {

    public static final int UTILS_PERMISSION_REQUEST_CODE = 8069;//工具类权限请求码

    private PermissionUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检查是否拥有指定的所有权限
     *
     * @param mContext
     * @param normalPermissions
     * @return
     */
    public static boolean checkPermissionAllGranted(Context mContext, List<String>
            normalPermissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : normalPermissions) {
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
     * @param mContext
     * @param normalPermissions
     * @param appOpsPermissions 为了适配小米
     * @return
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
                        LogUtils.d("PermissionUtils", "权限:允许");
                        checkOpResult = true;
                        break;
                    case AppOpsManager.MODE_IGNORED:
                        LogUtils.d("PermissionUtils", "权限:拒绝");
                        checkOpResult = false;
                        break;
                    case AppOpsManager.MODE_ERRORED:
                        LogUtils.d("PermissionUtils", "权限:错误");
                        checkOpResult = false;
                        break;
                    case 4:
                        LogUtils.d("PermissionUtils", "权限:询问");
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
     * 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
     *
     * @param activity
     * @param normalPermissions
     */
    public static void requestPermission(Activity activity, List<String> normalPermissions) {
        int normalPermissionsNumber = normalPermissions.size();
        ActivityCompat.requestPermissions(activity, normalPermissions.toArray(new
                String[normalPermissionsNumber]), UTILS_PERMISSION_REQUEST_CODE);
    }
}
