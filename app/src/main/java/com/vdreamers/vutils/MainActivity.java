package com.vdreamers.vutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepoem.vutils.vutils.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VUtils.sayHello();
    }
}