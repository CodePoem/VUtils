package com.vdreamers.vutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.codepoem.vutils.vutils.R;
import com.vdreamers.vutilsandroid.StorageUtils;

import java.io.File;

/**
 * 存储目录相关Activity
 * <p>
 * date 2019/06/03 10:12:26
 *
 * @author <a href="mailto:danhuangpai@2dfire.com">蛋黄派</a>
 */
public class StorageActivity extends AppCompatActivity {

    /**
     * 存储目录相关Activity启动类
     *
     * @param context 调用方上下文
     */
    public static final void actionStart(Context context) {
        Intent intent = new Intent(context, StorageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vs_activity_storage);

        TextView tvInternalDir = findViewById(R.id.tv_internal_dir_content);
        tvInternalDir.setText(StorageUtils.getInternalDir().getAbsolutePath());

        TextView tvInternalFileDir = findViewById(R.id.tv_internal_dir_file_content);
        tvInternalFileDir.setText(StorageUtils.getInternalFilesDir(StorageActivity.this).getAbsolutePath());

        TextView tvInternalCacheDir = findViewById(R.id.tv_internal_dir_cache_content);
        tvInternalCacheDir.setText(StorageUtils.getInternalCacheDir(StorageActivity.this).getAbsolutePath());

        TextView tvExternalDir = findViewById(R.id.tv_external_dir_content);
        tvExternalDir.setText(StorageUtils.getExternalDir().getAbsolutePath());

        TextView tvExternalPrivateFileDir = findViewById(R.id.tv_external_dir_private_file_content);
        StringBuilder externalPrivateFileDir = new StringBuilder();
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this, null).getAbsolutePath());
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_MUSIC));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_PODCASTS));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_RINGTONES));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_ALARMS));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_NOTIFICATIONS));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_PICTURES));
        externalPrivateFileDir.append("\n");
        externalPrivateFileDir.append(StorageUtils.getExternalPrivateFilesDir(StorageActivity.this,
                Environment.DIRECTORY_MOVIES));
        tvExternalPrivateFileDir.setText(externalPrivateFileDir);

        TextView tvExternalCacheDir = findViewById(R.id.tv_external_dir_private_cache_content);
        tvExternalCacheDir.setText(StorageUtils.getExternalPrivateCacheDir(StorageActivity.this).getAbsolutePath());

        TextView tvExternalPublicDir = findViewById(R.id.tv_external_dir_public_content);
        StringBuilder externalPublicFileDir = new StringBuilder();
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_MUSIC));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_PODCASTS));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_RINGTONES));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_ALARMS));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_NOTIFICATIONS));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_PICTURES));
        externalPublicFileDir.append("\n");
        externalPublicFileDir.append(StorageUtils.getExternalPublicDir(Environment.DIRECTORY_MOVIES));
        tvExternalPublicDir.setText(externalPublicFileDir);

        TextView tvInternalTotalSize = findViewById(R.id.tv_internal_total_size_content);
        long internalTotalSize = StorageUtils.getInternalTotalSize();
        StringBuilder strInternalTotalSize = new StringBuilder();
        strInternalTotalSize.append((double)internalTotalSize);
        strInternalTotalSize.append("\n");
        strInternalTotalSize.append((double)internalTotalSize / (1000L * 1000L));
        strInternalTotalSize.append(" M\n");
        strInternalTotalSize.append((double)internalTotalSize / (1000L * 1000L * 1000L));
        strInternalTotalSize.append(" G\n");
        tvInternalTotalSize.setText(strInternalTotalSize);

        TextView tvInternalFreeSize = findViewById(R.id.tv_internal_free_size_content);
        long internalFreeSize = StorageUtils.getInternalFreeSize();
        StringBuilder strInternalFreeSize = new StringBuilder();
        strInternalFreeSize.append((double)internalFreeSize);
        strInternalFreeSize.append("\n");
        strInternalFreeSize.append((double)internalFreeSize / (1000L * 1000L));
        strInternalFreeSize.append(" M\n");
        strInternalFreeSize.append((double)internalFreeSize / (1000L * 1000L * 1000L));
        strInternalFreeSize.append(" G\n");
        tvInternalFreeSize.setText(strInternalFreeSize);

        TextView tvExternalTotalSize = findViewById(R.id.tv_external_total_size_content);
        long lSDCardTotalSize = StorageUtils.getExternalTotalSize();
        StringBuilder strSDCardTotalSize = new StringBuilder();
        strSDCardTotalSize.append((double)lSDCardTotalSize);
        strSDCardTotalSize.append("\n");
        strSDCardTotalSize.append((double)lSDCardTotalSize / (1000L * 1000L));
        strSDCardTotalSize.append(" M\n");
        strSDCardTotalSize.append((double)lSDCardTotalSize / (1000L * 1000L * 1000L));
        strSDCardTotalSize.append(" G\n");
        tvExternalTotalSize.setText(strSDCardTotalSize);

        TextView tvExternalFreeSize = findViewById(R.id.tv_external_free_size_content);
        long lSDCardFreeSize = StorageUtils.getExternalFreeSize();
        StringBuilder strSDCardFreeSize = new StringBuilder();
        strSDCardFreeSize.append((double)lSDCardFreeSize);
        strSDCardFreeSize.append("\n");
        strSDCardFreeSize.append((double)lSDCardFreeSize / (1000L * 1000L));
        strSDCardFreeSize.append(" M\n");
        strSDCardFreeSize.append((double)lSDCardFreeSize / (1000L * 1000L * 1000L));
        strSDCardFreeSize.append(" G\n");
        tvExternalFreeSize.setText(strSDCardFreeSize);
    }
}
