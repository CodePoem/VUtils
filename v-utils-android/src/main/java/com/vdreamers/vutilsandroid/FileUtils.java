package com.vdreamers.vutilsandroid;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * 文件相关工具类
 * <p>
 * date 2019/03/19 17:13:56
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class FileUtils {
    /**
     * TAG for LogUtils messages.
     */
    private static final String TAG = "FileUtils";
    /**
     * 缓冲大小
     */
    private static final int BUFFER_SIZE = 1024 * 2;
    /**
     * 默认IO缓存目录名
     */
    private static final String DEFAULT_IO_CACHE_DIR_NAME = "V_UTILS_IO";

    /**
     * 根据uri获取文件path
     *
     * @param context 调用方上下文
     * @param uri     文件Uri
     * @return 文件path
     */
    public static String getFilePathFromContentUri(Context context, Uri uri) {
        String photoPath = "";
        if (context == null || uri == null) {
            return photoPath;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (isExternalStorageDocument(uri)) {
                if (TextUtils.isEmpty(docId)) {
                    return photoPath;
                }
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            } else if (isDownloadsDocument(uri)) {
                if (TextUtils.isEmpty(docId)) {
                    return photoPath;
                }
                try {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse
                            ("content://downloads/public_downloads"), Long.valueOf(docId));
                    photoPath = getDataColumn(context, contentUri, null, null);
                } catch (NumberFormatException e) {
                    LogUtils.i(TAG, String.format(Locale.getDefault(), "isDownloadsDocument: [%s]",
                            e.getMessage()));
                    return null;
                }
            } else if (isMediaDocument(uri)) {
                if (TextUtils.isEmpty(docId)) {
                    return photoPath;
                }
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if ("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            photoPath = getFilePathFromURI(context, uri);
        } else {
            photoPath = getFilePathFromURI(context, uri);
        }

        return photoPath;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs
                    , null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (IllegalArgumentException e) {
            LogUtils.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]",
                    e.getMessage()));
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        // copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File cacheDir = getExternalImageCacheDir(context, DEFAULT_IO_CACHE_DIR_NAME);
            File copyFile = new File(cacheDir + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static File getExternalImageCacheDir(Context context, String dirName) {
        File externalCacheDir = getExternalCacheDir(context);
        if (externalCacheDir != null) {
            String path = externalCacheDir.getPath() + dirName;
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache" + dirName;
        return new File(cacheDir);
    }

    public static File getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache";
            file = new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
        }
        return file;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) {
            return null;
        }
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) {
                return;
            }
            OutputStream outputStream = new FileOutputStream(dstFile);
            copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                LogUtils.i(TAG, String.format(Locale.getDefault(), "io-copy-out-close: [%s]",
                        e.getMessage()));
            }
            try {
                in.close();
            } catch (IOException e) {
                LogUtils.i(TAG, String.format(Locale.getDefault(), "io-copy-in-close: [%s]",
                        e.getMessage()));
            }
        }
        return count;
    }
}
