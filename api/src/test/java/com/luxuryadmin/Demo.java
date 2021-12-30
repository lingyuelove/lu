package com.luxuryadmin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luxuryadmin.common.encrypt.DESEncrypt;
import com.luxuryadmin.common.utils.LocalUtils;

import java.util.*;

/**
 * @author monkey king
 * @date 2019-12-16 18:28:51
 */
public class Demo {

    public static void main(String[] args) {
        //String json = "{\"token\":\"4267437c-be0d-4959-848d-9b73deb463b8\",\"shopList\":[{\"shopName\":\"足浴店\",\"shopNumber\":111111,\"shopId\":10000,\"type\":\"职员\"},{\"shopName\":\"水果店\",\"shopNumber\":22222,\"shopId\":10001,\"type\":\"职员\"},{\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"shopId\":10002,\"type\":\"职员\"}]}";

        /*String json = "{\"shopList\":[{\"shopId\":10000,\"shopName\":\"足浴店\",\"shopNumber\":111111,\"type\":\"职员\"},{\"shopId\":10001,\"shopName\":\"水果店\",\"shopNumber\":22222,\"type\":\"职员\"},{\"shopId\":10002,\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"type\":\"职员\"}],\n" +
                "\"shopList2\":{\"data2\":[{\"shopName\":\"足浴店\",\"shopNumber\":111111,\"shopId\":10000,\"type\":\"职员\"},{\"shopName\":\"水果店\",\"shopNumber\":22222,\"shopId\":10001,\"type\":\"职员\"},{\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"shopId\":10002,\"type\":\"职员\"}]},\n" +
                "\"token\":\"1101618f-cfcc-4e61-8a9c-b2c8de7e08d3\"}";*/

        String json = "{\"is_aes\":\"0\",\"is_sign\":\"0\"}";

        //String json = "{\"roken\":\"4267437c-be0d-4959-848d-9b73deb463b8\"}";

        JSONObject jsonObject = LocalUtils.sortJSONObject(JSONObject.parseObject(json));

        System.out.println(jsonObject.toString());



        /* System.out.println(DESEncrypt.md5Hex(json));
        Map obj = (Map) JSONArray.toJSON(json);
        System.out.println(obj.toString());
        TreeMap<String, Object> hashMap = new TreeMap<>(obj);
        System.out.println(hashMap.toString());
        //System.out.println(DESEncrypt.md5Hex());
        System.out.println(JSONObject.parseObject(json));
        System.out.println();*/
    }




    public static void sort() {
        Map map = new TreeMap<>();
        HashMap<String, Object> hashMap = new HashMap<>(map);
        hashMap.put("shopNumber", "13");
        hashMap.put("roken", "12");
        hashMap.put("shopList", "11");
        hashMap.put("type", "14");
        hashMap.put("a", "10");
        System.out.println("======hashmap=======");
        hashMap.forEach((s, s2) -> {
            System.out.println(s);
            System.out.println(s2);
        });
        System.out.println("======treeMap=======");
        TreeMap<String, Object> treeMap = new TreeMap<>(hashMap);
        treeMap.forEach((s, s2) -> {
            System.out.println(s);
            System.out.println(s2);
        });
    }
}
