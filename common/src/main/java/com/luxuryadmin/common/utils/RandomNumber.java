package com.luxuryadmin.common.utils;

import java.util.*;

/**
 * 生成随机编号工具类
 *
 * @author monkey king
 * @date 2019-12-05 16:57:52
 */
public class RandomNumber {

    public static void cc(int n) {
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = i;
        }
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int in = r.nextInt(n - i) + i;
            int t = x[in];
            x[in] = x[i];
            x[i] = t;
        }
        System.out.println(Arrays.toString(x));
    }


    /**
     * 随机生成纯数字编号<br/>
     *
     * @param startNum
     * @param endNum
     * @return
     */
    public static List<Integer> generatorNumber(int startNum, int endNum) {

        if (startNum < 0 || endNum < 0) {
            throw new NumberFormatException("startNum 和 endNum 必须为正整数");
        }
        if (endNum < startNum) {
            throw new NumberFormatException("必须 endNum ≥ startNum");
        }
        List<Integer> numberList = new ArrayList<>();
        for (int i = startNum; i <= endNum; i++) {
            numberList.add(i);
        }
        Collections.shuffle(numberList);
        // System.out.println(Arrays.toString(numberList.toArray()));
        return numberList;
    }

    /**
     * 对号码进行过滤;<br/>
     * map.put("same", same);豹子号<br/>
     * map.put("moreWithOne", moreWithOne);多带1<br/>
     * map.put("moreWithMore", moreWithMore);多带多<br/>
     * map.put("ascOrDesc", ascOrDesc);全升序或者全降序<br/>
     * map.put("normal", normal);<br/>
     *
     * @param list
     * @return
     */
    public static Map<String, List<Integer>> filterNiceNumber(List<Integer> list) {
        HashMap<String, List<Integer>> map = new HashMap<>(16);
        List<Integer> same = new ArrayList<>();
        List<Integer> moreWithOne = new ArrayList<>();
        List<Integer> moreWithMore = new ArrayList<>();
        List<Integer> ascOrDesc = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();
        for (Integer num : list) {

            String result = filterNiceNumber(num + "");

            switch (result) {
                case "20":
                    same.add(num);
                    break;
                case "21":
                    ascOrDesc.add(num);
                    break;
                case "22":
                    moreWithOne.add(num);
                    break;
                case "23":
                    moreWithMore.add(num);
                    break;
                default:
                    normal.add(num);
                    break;
            }
        }
        map.put("same", same);
        map.put("moreWithOne", moreWithOne);
        map.put("moreWithMore", moreWithMore);
        map.put("ascOrDesc", ascOrDesc);
        map.put("normal", normal);
        //System.out.println("sameNumber: " + same.size() + Arrays.toString(same.toArray()));
        //System.out.println("moreWithOne: " + moreWithOne.size() + Arrays.toString(moreWithOne.toArray()));
        //System.out.println("moreWithMore: " + moreWithMore.size() + Arrays.toString(moreWithMore.toArray()));
        //System.out.println("ascOrDesc: " + ascOrDesc.size() + Arrays.toString(ascOrDesc.toArray()));
        //System.out.println("normal: "+normal.size()+Arrays.toString(normal.toArray()));
        return map;
    }

    /**
     * 靓号状态：10:普通号; 20:豹子号(所有都一样aaaaa);21:连号(abcde);22:多带1(aaaaab);23多带2(aaabb);50:特殊数字靓号(例如5201314)
     *
     * @param num
     * @return
     */
    public static String filterNiceNumber(String num) {
        //豹子号
        boolean isSame = NumberPattern.same(num + "", 4);
        if (isSame) {
            return "20";
        }
        boolean isAscOrDesc = NumberPattern.ascOrDesc(num + "", 4);
        if (isAscOrDesc) {
            return "21";
        }
        //多带1
        boolean isMoreWithOne = NumberPattern.moreWithOne(num + "");
        if (isMoreWithOne) {
            return "22";
        }
        //多带多
        boolean isMoreWithMore = NumberPattern.moreWithMore(num + "");
        if (isMoreWithMore) {
            return "23";
        }
        //普通号
        return "10";
    }

    public static final String randomStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(26);
            sb.append(randomStr.charAt(number));
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        List<String> arrList = new ArrayList<>();
        List<String> lnkList = new LinkedList<>();
        //generatorNumber(2);
        //add(arrList);
        //add(lnkList);
        long st = System.currentTimeMillis();
        List<Integer> listNumber = generatorNumber(100000, 200000);
        filterNiceNumber(listNumber);
        long et = System.currentTimeMillis();
        System.out.println(et - st);
    }
}
