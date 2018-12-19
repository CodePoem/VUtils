package com.vdreamers.vutils;

import com.vdreamers.vutilsandroid.LogUtils;

/**
 * VUtils
 * <p>
 * date 2018/12/19 10:28:03
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class VUtils {

    private VUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void sayHello(){
        LogUtils.d("hello", "hello, dreamers~");
    }
}
