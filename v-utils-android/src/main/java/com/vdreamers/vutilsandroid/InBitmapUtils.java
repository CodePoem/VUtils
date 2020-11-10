package com.vdreamers.vutilsandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * Bitmap 重用工具类
 * <p>
 * date 2019/10/17 11:28:31
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class InBitmapUtils {

    /**
     * 计算inBitmap是否满足复用要求
     *
     * @param candidate     尝试复用的候选bitmap
     * @param targetOptions 目标选项参数
     * @return inBitmap是否满足复用要求
     */
    public static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {
        if (candidate == null) {
            return false;
        }

        // >= Android 4.4 (19) 版本，如果新的候选bitmap的实际字节大小比候选bitmap获得的分配字节大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int width = targetOptions.outWidth / targetOptions.inSampleSize;
            int height = targetOptions.outHeight / targetOptions.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
            return byteCount <= candidate.getAllocationByteCount();
        }

        // 在早期版本，要满足复用要求，必须尺寸完全一致，且inSampleSize必须为1
        return candidate.getWidth() == targetOptions.outWidth
                && candidate.getHeight() == targetOptions.outHeight
                && targetOptions.inSampleSize == 1;
    }

    /**
     * 返回基于bitmap的配置（像素存储方式）返回每像素存储的字节数
     *
     * @param config bitmap的配置（像素存储方式）
     * @return 每像素存储的字节数
     */
    public static int getBytesPerPixel(Bitmap.Config config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (config == Bitmap.Config.RGBA_F16) {
                return 8;
            }
        }
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

}
