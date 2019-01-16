package com.vdreamers.vutilsjava;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoneyFormatUtilsTest {

    @Test
    public void testFormatSpilt3end2() {
        Assert.assertEquals(MoneyFormatUtils.formatSpilt3end2(1111.11), "1,111.11");
        Assert.assertEquals(MoneyFormatUtils.formatSpilt3end2(0.1), "0.10");
        Assert.assertEquals(MoneyFormatUtils.formatSpilt3end2(0D), "0.00");
        Assert.assertEquals(MoneyFormatUtils.formatSpilt3end2(1D), "1.00");
        Assert.assertEquals(MoneyFormatUtils.formatSpilt3end2(11111111.1), "11,111,111.10");
    }
}