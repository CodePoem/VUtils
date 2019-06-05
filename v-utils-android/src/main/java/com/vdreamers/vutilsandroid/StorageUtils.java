package com.vdreamers.vutilsandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 存储相关工具类
 * <p>
 * date 2019/03/19 15:02:33
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class StorageUtils {

    public static final String TAG = StorageUtils.class.getSimpleName();

    public static final File NULL_FILE = new File("");

    private StorageUtils() {
        // cannot be instantiated
        throw new UnsupportedOperationException("StorageUtils cannot be instantiated");
    }

    /**
     * 获取内部存储目录
     *
     * @return file 内部存储目录
     */
    public static File getInternalDir() {
        return Environment.getDataDirectory();
    }

    /**
     * 获取内部存储目录普通文件目录
     *
     * @param context context 调用方上下文
     * @return file 内部存储目录普通文件目录
     */
    public static File getInternalFilesDir(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        return context.getFilesDir();
    }

    /**
     * 获取内部存储目录缓存目录
     *
     * @param context context 调用方上下文
     * @return file 内部存储目录缓存目录
     */
    public static File getInternalCacheDir(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        return context.getCacheDir();
    }

    /**
     * 删除内部存储文件
     *
     * @param context 调用方上下文
     * @param name    文件名
     * @return 删除是否成功
     */
    public static boolean deleteInternalFile(Context context, String name) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        return context.deleteFile(name);
    }

    /**
     * 获取内部存储目录所有文件列表
     *
     * @param context 调用方上下文
     * @return 文件名列表数组
     */
    public static String[] getInternalFileList(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        return context.fileList();
    }

    /**
     * 获取外部存储私有目录
     *
     * @return file 外部存储私有目录
     */
    public static File getExternalDir() {
        if (!isSDCardEnable()) {
            return NULL_FILE;
        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取主外部存储私有目录普通文件目录
     *
     * @param context context 调用方上下文
     * @param type    类型
     * @return file 主外部存储私有目录普通文件目录
     */
    public static File getExternalPrivateFilesDir(Context context, @Nullable String type) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (!isSDCardEnable()) {
            return NULL_FILE;
        }
        return context.getExternalFilesDir(type);
    }

    /**
     * 获取主外部存储私有目录缓存目录
     *
     * @param context context 调用方上下文
     * @return file 主外部存储私有目录缓存目录
     */
    public static File getExternalPrivateCacheDir(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        if (!isSDCardEnable()) {
            return NULL_FILE;
        }
        return context.getExternalCacheDir();
    }

    /**
     * 获取外部存储公共目录
     *
     * @return file 外部存储公共目录
     */
    public static File getExternalPublicDir(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (!isSDCardEnable()) {
            return NULL_FILE;
        }
        return Environment.getExternalStoragePublicDirectory(type);
    }

    /**
     * 判断SDCard是否可用
     *
     * @return SDCard是否可用 true：可用 false：不可用
     */
    public static boolean isSDCardEnable() {
        return (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable());
    }

    /**
     * 获取系统存储路径
     *
     * @return file 系统存储路径
     */
    public static File getRootDir() {
        return Environment.getRootDirectory();
    }

    /**
     * 获取外部存储的总容量 单位byte
     *
     * @return 外部存储的总容量 单位byte
     */
    public static long getExternalTotalSize() {
        if (isSDCardEnable()) {
            return getExternalDir().getTotalSpace();
        }
        return 0;
    }

    /**
     * 获取外部存储的剩余可用容量 单位byte
     *
     * @return 外部存储的剩余可用容量 单位byte
     */
    public static long getExternalFreeSize() {
        if (isSDCardEnable()) {
            return getExternalDir().getFreeSpace();
        }
        return 0;
    }

    /**
     * 获得内部存储的总容量 单位byte
     *
     * @return 内部存储的总容量 单位byte
     */
    public static long getInternalTotalSize() {
        return getInternalDir().getTotalSpace();
    }

    /**
     * 内部存储的剩余可用容量 单位byte
     *
     * @return 内部存储的剩余可用容量 单位byte
     */
    public static long getInternalFreeSize() {
        return getInternalDir().getFreeSpace();
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath 指定路径
     * @return 指定路径所在空间的剩余可用容量字节数
     */
    public static long getFreeBytes(String filePath) {
        StatFs statFs = new StatFs(filePath);
        // 单个数据块的大小（byte）
        long blockSize;
        // 空闲的数据块的数量
        long availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            availableBlocks = statFs.getAvailableBlocksLong();
        } else {
            blockSize = (long) statFs.getBlockSize();
            availableBlocks = (long) statFs.getAvailableBlocks();
        }
        return blockSize * availableBlocks;
    }

    /**
     * 获取指定路径所在空间的总容量字节数，单位byte
     *
     * @param filePath 指定路径
     * @return 指定路径所在空间的总容量字节数
     */
    public static long getTotalBytes(String filePath) {
        StatFs statFs = new StatFs(filePath);
        // 单个数据块的大小（byte）
        long blockSize;
        // 总数据块的数量
        long totalSize;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            totalSize = statFs.getBlockCountLong();
        } else {
            blockSize = (long) statFs.getBlockSize();
            totalSize = (long) statFs.getBlockCount();
        }
        return blockSize * totalSize;
    }

    /**
     * 获取app文件数据路径
     *
     * @param context 调用方上下文
     * @param dir     指定目录
     * @return app文件数据路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getFilesPath(Context context, String dir) {
        String directoryPath;
        if (isSDCardEnable()) {
            File file = context.getExternalFilesDir(dir);
            if (file != null) {
                // 外部存储可用
                directoryPath = file.getAbsolutePath();
            } else {
                directoryPath = context.getFilesDir() + File.separator + dir;
            }
        } else {
            // 外部存储不可用
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {
            // 判断文件目录不存在则创建
            file.mkdirs();
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG, directoryPath);
        }
        return directoryPath;
    }

    /**
     * 获取app缓存路径
     *
     * @param context 调用方上下文
     * @return app缓存路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getCachePath(Context context, String dir) {
        String directoryPath;
        if (isSDCardEnable()) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                // 外部存储可用
                directoryPath = file.getAbsolutePath();
            } else {
                directoryPath = context.getCacheDir() + File.separator + dir;
            }
        } else {
            // 外部存储不可用
            directoryPath = context.getCacheDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {
            // 判断文件目录不存在则创建
            file.mkdirs();
        }
        if (BuildConfig.DEBUG) {
            LogUtils.d(TAG, directoryPath);
        }
        return directoryPath;
    }

    /**
     * 存储图片到系统相册(默认以系统时间为文件名)
     *
     * @param context  调用方上下文
     * @param bmp      存储的图片Bitmap
     * @param dir      从存储的目录
     * @param fileName 存储自定义文件夹名
     * @return 是否保存成功 true：成功 false：失败
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String dir, String
            fileName) {
        // 首先保存图片
        String storePath = getFilesPath(context, dir);
        // 默认以系统时间为文件名
        if ("".equals(fileName) || fileName == null) {
            fileName = System.currentTimeMillis() + ".jpg";
        } else {
            fileName += ".jpg";
        }
        File file = new File(storePath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            // 通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            // 把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file
                    .getAbsolutePath(), fileName, null);

            // 保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
