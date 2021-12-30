package com.luxuryadmin.common.utils.weixxin.mpsdk;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

/**
 * @author monkey king
 * @date 2021-04-26 23:50:56
 */
@Slf4j
public class WxUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;

    public static String decode(String iv, String data, String key) throws Exception {
        log.debug("iv:{}", iv);
        log.debug("encryptedData:{}", data);
        log.debug("sessionKey:{}", key);
        String result = new String(decryptOfDiyIV(Base64.decode(data), Base64.decode(key), Base64.decode(iv)));
        log.info("=====wxUserInfoResult:{}", result);
        return result;
    }

    private static void init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @param ivs           自定义对称解密算法初始向量 iv
     * @return 解密后的字节数组
     */
    private static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }
}
