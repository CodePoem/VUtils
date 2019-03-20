package com.vdreamers.vutils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.ImageView;

import com.codepoem.vutils.vutils.BuildConfig;
import com.codepoem.vutils.vutils.R;
import com.vdreamers.vutilsandroid.LogUtils;
import com.vdreamers.vutilsandroid.PermissionUtils;
import com.vdreamers.vutilsandroid.StorageUtils;
import com.vdreamers.vutilsandroid.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

/**
 * 权限Activity
 * <p>
 * date 2019/03/16 22:12:42
 *
 * @author <a href="mailto:danhuangpai@2dfire.com">蛋黄派</a>
 */
public class PermissionActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_TAKE_PHOTO = 1;

    public static final int REQUEST_CODE_PICK_PHOTO = 2;
    private static final String TAG = "PermissionActivity";


    private ImageView mIvShowPhoto;
    private Uri mImageUri;
    private MediaRecorder mMediaRecorder;
    private Button mBtnRecordSound;
    private boolean mIsRecording;
    private String mFilePath;

    /**
     * 权限Activity启动类
     *
     * @param context
     */
    public static final void actionStart(Context context) {
        Intent intent = new Intent(context, PermissionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vs_activity_permission);
        mIvShowPhoto = findViewById(R.id.iv_show_photo);
        // 请求拍照权限
        findViewById(R.id.btn_request_take_photo).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.CAMERA);
            PermissionUtils.requestPermission(PermissionActivity.this, permissions);
        });
        // 拍照
        findViewById(R.id.btn_take_photo).setOnClickListener(v -> {
            takePhoto();
        });
        // 请求存储权限
        findViewById(R.id.btn_request_store).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            PermissionUtils.requestPermission(PermissionActivity.this, permissions);
        });
        // 选择图片
        findViewById(R.id.btn_select_photo).setOnClickListener(v -> {
            pickPhoto();
        });
        // 请求录音权限
        findViewById(R.id.btn_request_sound).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.RECORD_AUDIO);
            PermissionUtils.requestPermission(PermissionActivity.this, permissions);
        });
        // 录音
        mBtnRecordSound = findViewById(R.id.btn_record_sound);
        mBtnRecordSound.setOnClickListener(v -> {
            if (mIsRecording) {
                stopRecord();
            } else {
                List<String> permissions = new ArrayList();
                permissions.add(Manifest.permission.RECORD_AUDIO);
                if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                        permissions)) {
                    startRecord();
                } else {
                    PermissionUtils.requestPermission(PermissionActivity.this, permissions);
                }
            }
        });
        // 请求多个权限
        findViewById(R.id.btn_request_multi).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.RECORD_AUDIO);
            PermissionUtils.requestPermission(PermissionActivity.this, permissions);
        });
        // 检查拍照权限
        findViewById(R.id.btn_check_take_photo).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.CAMERA);
            if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                    permissions)) {
                ToastUtils.showToast("有权限~");
            } else {
                ToastUtils.showToast("无权限~");
            }
        });
        // 检查存储权限
        findViewById(R.id.btn_check_store).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                    permissions)) {
                ToastUtils.showToast("有权限~");
            } else {
                ToastUtils.showToast("无权限~");
            }
        });
        // 检查录音权限
        findViewById(R.id.btn_check_sound).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.RECORD_AUDIO);
            if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                    permissions)) {
                ToastUtils.showToast("有权限~");
            } else {
                ToastUtils.showToast("无权限~");
            }
        });
        // 检查多个权限
        findViewById(R.id.btn_check_multi).setOnClickListener(v -> {
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.RECORD_AUDIO);
            if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                    permissions)) {
                ToastUtils.showToast("有权限~");
            } else {
                ToastUtils.showToast("无权限~");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                Bitmap bitmap = null;
                try {
                    bitmap =
                            BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (StorageUtils.saveImageToGallery(PermissionActivity.this, bitmap, "vutils",
                        "")) {
                    ToastUtils.showToast("保存成功~");
                } else {
                    ToastUtils.showToast("保存失败~");
                }
                if (mIvShowPhoto != null) {
                    mIvShowPhoto.setImageBitmap(bitmap);
                }
            } else if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (mIvShowPhoto != null) {
                    mIvShowPhoto.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {
            File imageFile;
            List<String> permissions = new ArrayList();
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (PermissionUtils.checkPermissionAllGranted(PermissionActivity.this,
                    permissions)) {
                imageFile = createImageFile();
            } else {
                PermissionUtils.requestPermission(PermissionActivity.this, permissions);
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 7.0(API 24)以上要通过FileProvider将File转化为Uri
                mImageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                                ".fileprovider",
                        imageFile);
            } else {
                // 7.0(API 24)以下则直接使用Uri的fromFile方法将File转化为Uri
                mImageUri = Uri.fromFile(imageFile);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        // 图片类型
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    private void startRecord() {
        mIsRecording = true;
        mBtnRecordSound.setText("停止录音");
        // 开始录音
        // ①Initial：实例化MediaRecorder对象
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        try {
            // ②setAudioSource/setVedioSource
            // 设置麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式，H263视频/ARM音频编码)
            // 、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            String fileName = DateFormat.format("yyyyMMdd_HHmmss",
                    Calendar.getInstance(Locale.getDefault())) + ".m4a";
            mFilePath = StorageUtils.getFilesPath(PermissionActivity.this, fileName);
            /// ③准备
            mMediaRecorder.setOutputFile(mFilePath);
            mMediaRecorder.prepare();
            // ④开始
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            LogUtils.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            LogUtils.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    public void stopRecord() {
        mIsRecording = false;
        mBtnRecordSound.setText("开始录音");
        try {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mFilePath = "";
        } catch (RuntimeException e) {
            LogUtils.e(TAG, e.toString());
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(mFilePath);
            if (file.exists()) {
                file.delete();
            }

            mFilePath = "";
        }
    }
}
