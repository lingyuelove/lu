package com.luxuryadmin.common.utils;

import org.apache.commons.lang.ArrayUtils;

public class StringUtil extends org.apache.commons.lang.StringUtils {

    public static boolean isAnyBlank(String... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        }
        for (final String cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    public static String getAnyBlank(String[] strName, Object[] objValue) {
        if (objValue == null || strName == null || objValue.length - strName.length != 0) {
            return null;
        }
        for (int i = 0; i < objValue.length; i++) {
            if (objValue[i] == null || StringUtil.isBlank(String.valueOf(objValue[i]))) {
                return strName[i];
            }
        }
        return null;
    }


}