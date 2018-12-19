package com.vdreamers.vutilsandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtils {

    private StorageUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment
                .isExternalStorageRemovable());

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * 获取app文件数据路径
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getFilesPath(Context context, String dir) {
        String directoryPath;
        if (isSDCardEnable()) {
            //外部存储可用
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
        } else {
            //外部存储不可用
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        if (BuildConfig.DEBUG)
            Log.d("StorageUtils", directoryPath);
        return directoryPath;
    }

    /**
     * 获取app缓存路径
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context, String dir) {
        String directoryPath;
        if (isSDCardEnable()) {
            //外部存储可用
            directoryPath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            //外部存储不可用
            directoryPath = context.getCacheDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        if (BuildConfig.DEBUG)
            Log.d("StorageUtils", directoryPath);
        return directoryPath;
    }

    /**
     * 存储图片到系统相册(默认以系统时间为文件名)
     *
     * @param context
     * @param bmp
     * @param dir
     * @param fileName 存储自定义文件夹名
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String dir, String
            fileName) {
        // 首先保存图片
        String storePath = getFilesPath(context, dir);
        //默认以系统时间为文件名
        if (fileName == "" || fileName == null) {
            fileName = System.currentTimeMillis() + ".jpg";
        } else {
            fileName += ".jpg";
        }
        File file = new File(storePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file
                    .getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
