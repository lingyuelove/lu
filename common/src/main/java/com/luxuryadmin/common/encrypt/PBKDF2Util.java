package com.luxuryadmin.common.encrypt;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * PBKDF2算法<br/>
 * <a href='https://baike.baidu.com/item/PBKDF2/237696?fr=aladdin'>PBKDF2算法介绍</a>
 *
 * @author monkey king
 */
public class PBKDF2Util {

    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final String PRIVATE_KEY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /**
     * 生成密文的长度
     */
    private static final int HASH_BIT_SIZE = 128 * 4;
    /**
     * 迭代次数
     */
    private static final int PBKDF2_ITERATIONS = 1000;


    /**
     * 对输入的密码进行验证
     *
     * @param attemptedPassword 待验证密码
     * @param encryptedPassword 密文
     * @param salt              盐值
     * @return boolean
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @author monkey king
     */
    public static boolean authenticate(String attemptedPassword, String encryptedPassword, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 用相同的盐值对用户输入的密码进行加密
        String encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt+"");
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return encryptedAttemptedPassword.equals(encryptedPassword);
    }


    /**
     * 生成密文
     *
     * @param password 明文密码
     * @param salt     盐值
     * @return java.lang.String
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @author monkey king
     */
    public static String getEncryptedPassword(String password, String salt) throws NoSuchAlgorithmException,
            InvalidKeySpecException {

        //KeySpec spec = new PBEKeySpec(password.toCharArray(), fromHex(salt), PBKDF2_ITERATIONS, HASH_BIT_SIZE);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), PBKDF2_ITERATIONS, HASH_BIT_SIZE);
        SecretKeyFactory f = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return toHex(f.generateSecret(spec).getEncoded());
    }


    /**
     * 十六进制字符串转二进制字符串
     *
     * @param hex 要转换的字符串
     * @return 二进制字符串
     */
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }


    /**
     * 二进制字符串转十六进制字符串
     *
     * @param array 二进制字符串
     * @return java.lang.String
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * 获取盐<br/>;由26个小写字母+26个大写字母+10位阿拉伯数字,总共62位字符串组成的key;
     * 参数范围为[1,62]
     *
     * @param saltLength 盐的长度
     * @return 盐
     */
    public static String getSalt(int saltLength) {

        if (saltLength < 1 || saltLength > PRIVATE_KEY.length()) {
            saltLength = PRIVATE_KEY.length();
        }
        StringBuffer salt = new StringBuffer();
        char[] m = PRIVATE_KEY.toCharArray();
        for (int i = 0; i < saltLength; i++) {
            double random = Math.random();
            char c = m[(int) (random * PRIVATE_KEY.length())];
            salt = salt.append(c);
        }
        return salt.toString();
    }

    /**
     * 获取盐<br/>;
     * @param saltLength saltLength 盐的长度
     * @param fromKey 自定义盐的取值范围 如0123456789 则1~9的纯数字
     * @return 盐
     */
    public static String getSalt(int saltLength,String fromKey) {
        if (null == fromKey || "".equals(fromKey)) {
            fromKey = PRIVATE_KEY;
        }

        if (saltLength < 1 || saltLength > fromKey.length()) {
            saltLength = fromKey.length();
        }
        StringBuffer salt = new StringBuffer();
        char[] m = fromKey.toCharArray();
        for (int i = 0; i < saltLength; i++) {
            double random = Math.random();
            char c = m[(int) (random * fromKey.length())];
            salt = salt.append(c);
        }
        return salt.toString();
    }

    /**
     * 获取盐<br/>;由26个小写字母+26个大写字母+10位阿拉伯数字,总共62位字符串组成的key;<br/>
     * 默认生成32位;如需定义盐的长度,请使用getSalt(int saltLength);
     *
     * @return 长度为32的盐
     */
    public static String getSaltDefault() {
        return getSalt(32);
    }


    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        long st = System.currentTimeMillis();
        String salt = getSalt(32);
        System.out.println(salt);
        String saltPassword = getEncryptedPassword("123456", salt);
        System.out.println(saltPassword);
        System.out.println(authenticate("123456", saltPassword, salt));
        long et = System.currentTimeMillis();
        System.out.println(et - st);
    }
}