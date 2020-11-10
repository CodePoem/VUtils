package com.vdreamers.vutilsandroid;


import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 多媒体存储工具类
 * uri：用于检索内容的 URI
 * projection：要返回的列的列表。传递 null 时，将返回所有列，这样会导致效率低下
 * selection：一种用于声明要返回哪些行的过滤器，其格式为 SQL WHERE 子句（WHERE 本身除外）。传递 null 时，将为指定的 URI 返回所有行
 * selectionArgs：您可以在 selection 中包含 ?s，它将按照在 selection 中显示的顺序替换为 selectionArgs 中的值。该值将绑定为字串符
 * sortOrder：行的排序依据，其格式为 SQL ORDER BY 子句（ORDER BY 自身除外）。传递 null 时，将使用默认排序顺序（可能并未排序）
 * <p>
 * date 2019/06/06 15:08:23
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class MediaStoreUtils {

    public static List<Uri> getImageList(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }
        List<Uri> imageUris = new ArrayList<>();
        Uri exContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String idColumnName = MediaStore.Images.Media._ID;
        Cursor cursor = context.getContentResolver().query(
                exContentUri, new String[]{idColumnName}, null, null, null);
        if (cursor != null) {
            LogUtils.d("cursor size:" + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(idColumnName));
                Uri imageUri = ContentUris.withAppendedId(exContentUri, id);
                LogUtils.d("imageUri:" + imageUri);
                imageUris.add(imageUri);
            }
            cursor.close();
        }
        return imageUris;
    }
}
