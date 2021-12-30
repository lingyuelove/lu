package com.luxuryadmin.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @PackgeName: com.luxuryadmin.common.utils
 * @ClassName: DecimalFormatUtil
 * @Author: ZhangSai
 * Date: 2021/9/7 16:20
 */
public class DecimalFormatUtil {
    public static final String DEFAULT_FORMAT = "#,###.##";
    private DecimalFormatUtil() {

    }

    /**
     * 格式化数字表示
     * @param bigDecimal
     * @param format
     * @return
     */
    public static String formatString(BigDecimal bigDecimal, String format) {
        if(bigDecimal == null) {
            return "";
        }
        if(format == null || format.isEmpty()) {
            format = DEFAULT_FORMAT;
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(bigDecimal.doubleValue());
    }
   /* public static String formatString2(float data) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(data);
    }*/

    public static void main(String[] args) {
        System.out.println(DecimalFormatUtil.formatString(new BigDecimal("246913578.12"), null));
    }


}
