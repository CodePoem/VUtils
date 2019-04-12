package com.vdreamers.vutilsjava;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateUtilsTest {

    /**
     * 线程数
     */
    private static final int NUM_THREAD = 11;

    @Test
    public void format() {
        for (int i = 0; i < NUM_THREAD; ++i) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    if (finalI % 2 == 0) {
                        Assert.assertEquals("2019-04-12 17:22:59",
                                DateUtils.format(new Date(1555060979000L),
                                DateUtils.FORMAT_PATTERN));
                    } else {
                        Assert.assertEquals("2019-04-12", DateUtils.format(new Date(1555060979000L),
                                DateUtils.FORMAT_PATTERN4));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }
}