package com.vdreamers.vutils;

import android.os.Bundle;

import com.codepoem.vutils.vutils.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vs_activity_main);
        VUtils.sayHello();

        findViewById(R.id.btn_permission).setOnClickListener(v -> {
            PermissionActivity.actionStart(MainActivity.this);
        });

        findViewById(R.id.btn_storage).setOnClickListener(v -> {
            StorageActivity.actionStart(MainActivity.this);
        });
    }
}
