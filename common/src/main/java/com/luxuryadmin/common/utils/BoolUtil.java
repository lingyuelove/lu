package com.luxuryadmin.common.utils;

public class BoolUtil {

    /**
     * 把布尔型转为Integer类型
     * @param value
     * @return
     */
    public static Integer convertBooleanToInteger(Boolean value){
        return value ? 1 : 0;
    }

}
