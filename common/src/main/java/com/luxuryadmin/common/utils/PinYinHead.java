package com.luxuryadmin.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
/**
 * @Author:     Mong
 * @Date:    2021/5/27 21:35
 * @Description:  根据汉字生成拼音首字母大写拼接
 */
public class PinYinHead {

    /**
     * 得到中文首字母（清华大学 -> QHDX）
     * @param str 需要转化的中文字符串
     * @return 大写首字母缩写的字符串
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString().toUpperCase();
    }
}
