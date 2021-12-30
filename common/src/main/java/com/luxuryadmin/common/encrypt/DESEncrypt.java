package com.luxuryadmin.common.encrypt;


import com.luxuryadmin.common.utils.LocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DES加密工具类;<br/>
 * 加密和解密方法已经设置为私有属性;如需新增;请直接在本类内添加对应的加解密方法;
 *
 * @author monkey king
 * @ClassName DESEncrypt
 * @Date 2019-12-04 11:52:54
 * @Version 1.0
 */
@Slf4j
public class DESEncrypt {


    /**
     * 加密帐号和手机号的关键key;可反向解密;
     */
    private static final String USERNAME_KEY = "user_key";

    /**
     * 加密
     *
     * @param src 要加密的数据
     * @param key 加密取用的key。八位字符串
     * @return
     * @throws Exception
     */
    private static final String encode(String src, String key) throws Exception {
        if (LocalUtils.isEmptyAndNull(src)) {
            return "";
        }
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
        byte bb[] = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
        StringBuffer buff = new StringBuffer(bb.length);
        String sTemp;
        for (int i = 0; i < bb.length; i++) {
            sTemp = Integer.toHexString(0xFF & bb[i]);
            if (sTemp.length() < 2) {
                buff.append(0);
            }
            buff.append(sTemp.toUpperCase());
        }
        return buff.toString();
    }


    /**
     * 解密
     *
     * @param src 要解密的数据源
     * @param key 加密时取用的key，八位字符串
     * @return
     * @throws Exception
     */
    private static final String decode(String src, String key) throws Exception {
        if (LocalUtils.isEmptyAndNull(src)) {
            return "";
        }
        try {
            int len = (src.length() / 2);
            byte[] result = new byte[len];
            char[] chars = src.toCharArray();
            for (int j = 0; j < len; j++) {
                int pos = j * 2;
                result[j] = ((byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1])));
            }
            //获取初始化之后的Cipher对象;
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, key);
            // 正式执行解密操作
            return new String(cipher.doFinal(result));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return src;

    }

    /**
     * 获取初始化之后的Cipher对象;<br/>
     * 获取之后,可以直接cipher.doFinal();操作
     *
     * @param encryptMode Cipher.ENCRYPT_MODE,Cipher.DECRYPT_MODE
     * @param key         Key
     * @return Cipher对象;
     * @throws Exception
     */
    private static Cipher getCipher(int encryptMode, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 一个SecretKey对象
        SecretKey secretKey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(encryptMode, secretKey, sr);
        return cipher;
    }

    private static byte toByte(char c) {

        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 加密字符串;<br>
     * 帐号,手机号码;
     *
     * @param string 需要加密的字符串;
     * @return 加密过后的字符串;
     * @throws Exception
     */
    public static String encodeUsername(String string) {
        String str = null;
        try {
            str = encode(string, USERNAME_KEY);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return str;
    }


    /**
     * 解密字符串;<br>
     * 帐号,手机号码;
     *
     * @param string 加密过后的字符串;
     * @return 解密过后的字符串;
     * @throws Exception
     */
    public static String decodeUsername(String string) {
        String de = null;
        try {
            de = decode(string, USERNAME_KEY);
        } catch (Exception e) {
            log.error(e.getMessage() + "; str: " + string, e);
        }
        return de;
    }


    /**
     * 判断加密的字符串是否符合一致;<br>
     * <p>
     * 该方法一般和前后端使用;
     *
     * @param uCode
     * @return
     */
    public static boolean isCodePass(String uCode) {
        try {
            System.out.println("uCode: " + uCode);
            String result = "";
            //前后相差2分钟; 总共5个值;
            for (int i = -2; i <= 2; i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + i);
                //System.out.println(calendar.getTime());
                result += getEncryptString(calendar.getTime()) + ";";
            }
            System.out.println(result);
            if (!LocalUtils.isEmpty(result)) {
                String[] keys = result.split(";");
                for (String key : keys) {
                    if (uCode.equals(key)) {
                        return true;
                    }
                    //System.out.println("继续执行");
                }
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 根据一个时间;获取一串加密字符;
     *
     * @param date 时间;
     * @return 加密字符;
     */
    public static String getEncryptString(Date date) {
        String result = "";
        try {
            String key = "2F0A3C68DD9E3642291FA04BB54D9F428C70B45D6C74E99E8816DEB0D8AA9492A1C0F45F85B0DE8E";
            String time = new SimpleDateFormat("mm").format(date);
            long ddHHmmTime = Long.parseLong(new SimpleDateFormat("ddHHmm").format(date));
            //常量;
            int value = 666;
            int st = Integer.valueOf(time.substring(0, 1));
            int et = Integer.valueOf(time) + 1;
            String keySub = key.substring(st, et);
            //截取的字符串的总数值;
            int subKeyNum = changeLetterToNum(keySub);
            long resultNum = LocalUtils.calcNumber(ddHHmmTime, "+", value).longValue();
            //info("ddHHmmTime: "+ddHHmmTime+";分钟: "+time+"; 截取范围(索引0开始): "+(st)+"(含)-"+(et-1)+"(含);(包含起始值);截取字符串为: "+keySub+";字符串相加结果:　"+subKeyNum);
            resultNum = LocalUtils.calcNumber(resultNum, "+", subKeyNum).longValue();
            result = resultNum + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 对16进制的字符串进行换算;并对每个字符串换算过后的值进行相加;
     *
     * @param str 16进制的字符串;
     * @return 换算过后的并相加的值;
     */
    public static int changeLetterToNum(String str) {
        int num = 0;
        if (!LocalUtils.isEmptyAndNull(str)) {
            char[] chars = str.toCharArray();
            for (char c : chars) {
                String val = String.valueOf(c).toLowerCase();
                switch (val) {
                    case "a":
                        num += 10;
                        continue;
                    case "b":
                        num += 11;
                        continue;
                    case "c":
                        num += 12;
                        continue;
                    case "d":
                        num += 13;
                        continue;
                    case "e":
                        num += 14;
                        continue;
                    case "f":
                        num += 15;
                        continue;
                    default:
                        num += Integer.parseInt(val);
                        continue;
                }
            }
        }
        return num;
    }


    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes(StandardCharsets.UTF_8));
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    public static String md5(String str) {
        String digest = null;
        StringBuffer buffer = new StringBuffer();
        try {
            MessageDigest digester = MessageDigest.getInstance("md5");
            byte[] digestArray = digester.digest(str.getBytes());
            for (int i = 0; i < digestArray.length; i++) {
                buffer.append(String.format("%02x", digestArray[i]));
            }
            digest = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }


    public static void main(String[] args) throws Exception {
     /*   String username = encodeUsername("123456");
        System.out.println("username: " + username);
        System.out.println("username2: " + decodeUsername(username));
        System.out.println(getEncryptString(new Date()));
        System.out.println(isCodePass("92068"));*/
        //System.out.println(encodeUsername("15112304365"));
        //System.out.println(Base64.getEncoder().encodeToString("123456".getBytes(StandardCharsets.UTF_8)));

        String str = "code=ok;msg=操作成功;data=5e775a5ed663d3f75bbb6c18099394278fd53df0e5bd3db6810983b3b545533d";
        String md5 = md5Hex(str + "sign_key");
        System.out.println(md5);
        System.out.println(LocalUtils.isPasswordMD5(md5));
        System.out.println("8b08d1fefd93aaff5bccca851feb06fc".equalsIgnoreCase(md5));

        System.out.println("5e775a5ed663d3f75bbb6c18099394278fd53df0e5bd3db6810983b3b545533d".equals("5e775a5ed663d3f75bbb6c18099394278fd53df0e5bd3db6810983b3b545533d"));
        //670b14728ad9902aecba32e22fa4f6bd15868133544
        System.out.println(md5Hex("123456"));
        System.out.println(md5("000000"));
        System.out.println(DESEncrypt.encodeUsername("18768499377"));
        System.out.println(DESEncrypt.decodeUsername("944FD0802B1986F75CC25FD84F3904EC"));


    }
}
