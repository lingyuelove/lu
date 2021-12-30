package com.luxuryadmin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class JointUtil {

    /**
     * GET 请求拼接参数
     *
     * @param o   对象
     * @param url
     * @return
     */
    public static String urlJoint(Object o, String url) {
        String[] filedName = getFiledName(o);
        StringBuilder str = new StringBuilder();
        str.append(url);
        for (int i = 0; i < filedName.length; i++) {
            Object fieldValueByName = getFieldValueByName(filedName[i], o);
            if (fieldValueByName != null) {
                if (i == 0) {
                    str.append("?");
                } else {
                    str.append("&");
                }
                str.append(filedName[i] + "=" + fieldValueByName);
            }
        }
        return str.toString();
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            Object value = method.invoke(o);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }
}
