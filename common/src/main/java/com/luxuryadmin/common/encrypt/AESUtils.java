package com.luxuryadmin.common.encrypt;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 参数加密解密工具，不要和DESEncrypt弄混了
 * </p>
 *
 * @author monkey king
 * @date 2019-12-09 15:53:09
 */
@Slf4j
public class AESUtils {


    private static final String AES = "AES";

    private static final String CRYPT_KEY = "LuxuryAdmin12345";

    private static final String IV_STRING = "12345LuxuryAdmin";

    /**
     * 加密
     *
     * @param content 加密内容
     * @return 密文
     * @throws Exception e
     */
    public static String encrypt(String content) {
        if (content == null || "".equals(content)) {
            return "";
        }
        byte[] encryptedBytes = new byte[0];
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerate、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = CRYPT_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
        } catch (Exception e) {

            log.error("AES encrypt Exception,content = {},Exception = {}", content, e.getStackTrace());
        }

        // 同样对加密后数据进行 16进制
        return byteToHex(encryptedBytes);
    }

    /**
     * 解密
     *
     * @param content 密文
     * @return 明文
     * @throws Exception e
     */
    public static String decrypt(String content) {
        if (content == null || "".equals(content)) {
            return "";
        }
        // base64 解码
        try {
            byte[] encryptedBytes = hexToByte(content);
            byte[] enCodeFormat = CRYPT_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, AES);
            byte[] initParam = IV_STRING.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);

            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {

            log.error("AES decrypt Exception,content = {},Exception = {}", content, e.getStackTrace());
        }
        return null;
    }

    /**
     * byte数组转hex
     *
     * @param bytes
     * @return
     */
    private static String byteToHex(byte[] bytes) {
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            // 每个字节由两个字符表示，位数不够，高位补0
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
        }
        return sb.toString().trim();
    }


    /**
     * hex转byte数组
     *
     * @param hex
     * @return
     */
    private static byte[] hexToByte(String hex) {
        int m = 0, n = 0;
        // 每两个字符描述一个字节
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte) intVal);
        }
        return ret;
    }


    public static void main(String[] args) {
        String encryptString = encrypt("{\"a\":[{\"shopId\":10000,\"shopName\":\"足浴店\",\"shopNumber\":111111,\"type\":\"职员\"},{\"shopId\":10001,\"shopName\":\"水果店\",\"shopNumber\":22222,\"type\":\"职员\"},{\"shopId\":10002,\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"type\":\"职员\"}],\"shopList\":[{\"shopId\":10000,\"shopName\":\"足浴店\",\"shopNumber\":111111,\"type\":\"职员\"},{\"shopId\":10001,\"shopName\":\"水果店\",\"shopNumber\":22222,\"type\":\"职员\"},{\"shopId\":10002,\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"type\":\"职员\"}],\"shopList2\":{\"data2\":[{\"shopId\":10000,\"shopName\":\"足浴店\",\"shopNumber\":111111,\"type\":\"职员\"},{\"shopId\":10001,\"shopName\":\"水果店\",\"shopNumber\":22222,\"type\":\"职员\"},{\"shopId\":10002,\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"type\":\"职员\"}],\"data3\":{\"shopList3\":[{\"shopId\":10000,\"shopName\":\"足浴店\",\"shopNumber\":111111,\"type\":\"职员\"},{\"shopId\":10001,\"shopName\":\"水果店\",\"shopNumber\":22222,\"type\":\"职员\"},{\"shopId\":10002,\"shopName\":\"十足便利店\",\"shopNumber\":88888,\"type\":\"职员\"}],\"token\":\"e40b3512-6236-485d-87d3-27844505550f\"}},\"token\":\"e40b3512-6236-485d-87d3-27844505550f\"}");
         //System.out.println("加密:" + encryptString);
        //System.out.println("解密:" + decrypt(encryptString));
        System.out.println("解密:" + decrypt("975331235ca660ce9359ee5054806b4b46921ceb5753f0c04f6c99c7c8dc7062="));
    }



}