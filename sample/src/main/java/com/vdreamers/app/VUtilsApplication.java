package com.vdreamers.app;

import android.app.Application;

import com.vdreamers.vutilsandroid.ToastUtils;

/**
 * 自定义Application
 * <p>
 * date 2019/03/19 16:20:31
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class VUtilsApplication extends Application {

    private static VUtilsApplication sInstance;

    private static VUtilsApplication getInstance(){
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ToastUtils.init(sInstance);
    }

}
