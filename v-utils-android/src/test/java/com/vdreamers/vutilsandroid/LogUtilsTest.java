package com.vdreamers.vutilsandroid;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class LogUtilsTest {

    private static final String TAG = "LogUtilsTest";
    private static final String MSG_V = "V";
    private static final String MSG_D = "D";
    private static final String MSG_I = "I";
    private static final String MSG_W = "W";
    private static final String MSG_E = "E";

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }


    @Test
    public void testV() {
        LogUtils.v(TAG, MSG_V);
        List<ShadowLog.LogItem> logItems = ShadowLog.getLogsForTag(TAG);
        switch (LogUtils.LEVEL) {
            case LogUtils.VERBOSE:
                Assert.assertEquals(logItems.get(0).msg, MSG_V);
                break;
            case LogUtils.DEBUG:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.INFO:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.WARN:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.ERROR:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.NOTHING:
                Assert.assertEquals(logItems.size(), 0);
                break;
            default:
                break;
        }
    }

    @Test
    public void testD() {
        LogUtils.d(TAG, MSG_D);
        List<ShadowLog.LogItem> logItems = ShadowLog.getLogsForTag(TAG);
        switch (LogUtils.LEVEL) {
            case LogUtils.VERBOSE:
                Assert.assertEquals(logItems.get(0).msg, MSG_D);
                break;
            case LogUtils.DEBUG:
                Assert.assertEquals(logItems.get(0).msg, MSG_D);
                break;
            case LogUtils.INFO:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.WARN:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.ERROR:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.NOTHING:
                Assert.assertEquals(logItems.size(), 0);
                break;
            default:
                break;
        }
    }

    @Test
    public void testI() {
        LogUtils.i(TAG, MSG_I);
        List<ShadowLog.LogItem> logItems = ShadowLog.getLogsForTag(TAG);
        switch (LogUtils.LEVEL) {
            case LogUtils.VERBOSE:
                Assert.assertEquals(logItems.get(0).msg, MSG_I);
                break;
            case LogUtils.DEBUG:
                Assert.assertEquals(logItems.get(0).msg, MSG_I);
                break;
            case LogUtils.INFO:
                Assert.assertEquals(logItems.get(0).msg, MSG_I);
                break;
            case LogUtils.WARN:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.ERROR:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.NOTHING:
                Assert.assertEquals(logItems.size(), 0);
                break;
            default:
                break;
        }
    }

    @Test
    public void testW() {
        LogUtils.w(TAG, "W");
        List<ShadowLog.LogItem> logItems = ShadowLog.getLogsForTag(TAG);
        switch (LogUtils.LEVEL) {
            case LogUtils.VERBOSE:
                Assert.assertEquals(logItems.get(0).msg, MSG_W);
                break;
            case LogUtils.DEBUG:
                Assert.assertEquals(logItems.get(0).msg, MSG_W);
                break;
            case LogUtils.INFO:
                Assert.assertEquals(logItems.get(0).msg, MSG_W);
                break;
            case LogUtils.WARN:
                Assert.assertEquals(logItems.get(0).msg, MSG_W);
                break;
            case LogUtils.ERROR:
                Assert.assertEquals(logItems.size(), 0);
                break;
            case LogUtils.NOTHING:
                Assert.assertEquals(logItems.size(), 0);
                break;
            default:
                break;
        }
    }

    @Test
    public void testE() {
        LogUtils.e(TAG, "E");
        List<ShadowLog.LogItem> logItems = ShadowLog.getLogsForTag(TAG);
        switch (LogUtils.LEVEL) {
            case LogUtils.VERBOSE:
                Assert.assertEquals(logItems.get(0).msg, MSG_E);
                break;
            case LogUtils.DEBUG:
                Assert.assertEquals(logItems.get(0).msg, MSG_E);
                break;
            case LogUtils.INFO:
                Assert.assertEquals(logItems.get(0).msg, MSG_E);
                break;
            case LogUtils.WARN:
                Assert.assertEquals(logItems.get(0).msg, MSG_E);
                break;
            case LogUtils.ERROR:
                Assert.assertEquals(logItems.get(0).msg, MSG_E);
                break;
            case LogUtils.NOTHING:
                Assert.assertEquals(logItems.size(), 0);
                break;
            default:
                break;
        }
    }
}