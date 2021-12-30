package com.luxuryadmin.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberPattern {

    public static void main(String[] args) {
        //匹配6位顺增
        String number = "13332";
        moreWithMore(number);
        String number2 = "122233";
        moreWithMore(number2);
        String number3 = "222333";
        moreWithMore(number3);
        String number4 = "11113";
        moreWithMore(number4);
        String number5 = "331222";
        moreWithMore(number5);
        String number6 = "12222";
        moreWithMore(number6);
        String number7 = "1100000";
        moreWithMore(number7);
        String number8 = "1100000";
        moreWithOne(number8);

        System.out.println(ascOrDesc("12345", 5));
    }

    /**
     * 匹配3位以上的重复数字
     */
    public static boolean same(String number, int digit) {
        String pattern = "([\\d])\\1{" + (digit - 1) + ",}";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
        boolean isTrue = ma.matches();
        //System.out.println(isTrue);
        return isTrue;
    }

    /**
     * xxbbbb | bbbbxx
     * 多带多
     *
     * @param number
     */
    public static boolean moreWithMore(String number) {
        //String pattern = "([\\d])\\1*([\\d])\\2{2}";
        String pattern = "([\\d])\\1+([\\d])\\2+";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
        boolean isTrue = ma.matches();
        //System.out.println(number + ":moreWithMore:" + isTrue);
        return isTrue;
    }

    /**
     * 多带1
     *
     * @param number
     * @return
     */
    public static boolean moreWithOne(String number) {
        //String pattern = "([\\d])\\1*([\\d])\\2{2}";
        //String pattern = "([\\d])\\1([\\d])\\2* | ([\\d])\\3*([\\d])\\4";
        String pattern = "([\\d])\\1*\\d|\\d([\\d])\\2*";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
        boolean isTrue = ma.matches();
        //System.out.println(number+":moreWithOne:"+isTrue);
        return isTrue;
    }

    /**
     * 匹配4-9位连续的数字
     */
    public static void serial(String number) {
        String pattern = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
    }

    /**
     * 匹配N位升序或降序
     */
    public static boolean ascOrDesc(String number, int digit) {
        //String pattern = String.format("(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){%d,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){%d,})\\d", (digit - 1), (digit - 1));
        String pattern = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){" + (digit - 1) + ",}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){" + (digit - 1) + ",})\\d";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
        boolean isTrue = ma.matches();
        return isTrue;
    }

    /**
     * -1降序(连续)
     *
     * @param number
     * @param digit  连续位数
     */
    public static void desc(String number, int digit) {

        String pattern = "(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){" + (digit - 1) + ",}\\d";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
    }

    /**
     * +1升序(连续)
     *
     * @param number
     * @param digit  连续位数
     */
    public static void asc(String number, int digit) {
        String pattern = "(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){" + (digit - 1) + ",}\\d";
        Pattern pa = Pattern.compile(pattern);
        Matcher ma = pa.matcher(number);
    }
}
