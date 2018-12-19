package com.vdreamers.vutilsjava;

import java.text.DecimalFormat;

/**
 * 金钱数额格式化工具
 * <p>
 * date 2018/12/18 12:53:38
 *
 * @author <a href="mailto:codepoetdream@gmail.com">Mr.D</a>
 */
public class MoneyFormatUtils {

    /**
     * 金额格式-保留两位小数，小数点前千位逗号
     */
    public static final String PATTERN_SPILT_3_END_2 = "#,##0.00";

    /**
     * 格式化金额显示
     * 1111.11 - "1,111.11"
     * 0.1 - "0.10"
     *
     * @param money 格式化前金额
     * @return 格式化后金额字符串
     */
    public static String formatSpilt3end2(Double money) {
        if (money == null) {
            return "0.00";
        }
        return new DecimalFormat(PATTERN_SPILT_3_END_2).format(money);
    }
}
