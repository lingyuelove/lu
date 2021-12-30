package com.luxuryadmin.service.pay;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 微信解密工具类
 *
 * @author Administrator
 */
public class AesUtilWeiXin {

    private static final int KEY_LENGTH_BYTE = 32;
    private static final int TAG_LENGTH_BIT = 128;
    private final byte[] aesKey;

    public AesUtilWeiXin(byte[] key) {
        if (key.length != KEY_LENGTH_BYTE) {
            throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
        }
        this.aesKey = key;
    }

    public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext)
            throws GeneralSecurityException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);

            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), "utf-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }


    //public static void main(String[] args) throws GeneralSecurityException, IOException {
    //
    //    String apiVeKey = "43f71e0c8a08459197427588c425125d";
    //    String associated_data = "transaction";
    //    String nonce = "dxGfKclfqvri";
    //    String ciphertext = "D7djJ3O9PuAb0eS4VY97WwkvZ19YSnTivNRwRLMHxtsbG/rBptAn1jria6xXvIFY+UTj257LZYl6JM/JtiYezY5M3hCrr7qn68stqipWXSbRyMajnhiG7/l0SuVxV2yMxod0XN7R0BSJYr22s9gGNoAwJIEhtM+KkAAgTkVuoZTmRW8X9bSNwYS8i1Cj/V4AMB50sFfVXVXWg8yuUBxjywLtR9FjZyA+qs4EVifgofqysTKLgxVfoatOpKJW1SKBWROP4pGmQVaBYZQ9kIQmCy4O6HM4fE/omf2rs3JYuIr3y2I3snSgx3bNyLKYp59/6e56QQEYoNF8PZffxlA5qBQ2ksSUhq8SqEYkOP8gKWTMzZPTfAXqHd71Y5vHC5cedDPh+WJCJ6y80GRLNRY73KXT8gegeO6zlOoKqJjSIfip+c0WUH2jLQbnJVE+Fo0NxWa5qeREqSYCNpoo7HO1lF/VvYd1S+suOacEmmaK1kjnD347Pe8tPfmZRMuLEfW9cqQWcf8y8EBDGKmbyweCiGt4HgEJmPc8+nqaxkrS0dYGBpvk1DhBbIea40fzhXy4EUTHaaBHuUtN3a3XlQ8=";
    //    String s = new AesUtilWeiXin(apiVeKey.getBytes()).decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);
    //    System.out.println(s);
    //}

    public static void main(String[] args) throws GeneralSecurityException, IOException {

        String apiVeKey = "43f71e0c8a08459197427588c425125d";
        String associated_data = "transaction";
        String nonce = "QtyQzK25QLpT";
        String ciphertext = "vTxP1bMugX4f6Yx4+f7CPKlPoV5IEDSL2bqasmnO+t4yva8s0e9z1ZzUtQzT+HJ6wDMTOHssGUnnvky1hjGQ+FxcM48WIkt7MUecJrGlK2M9crjJVh2rE+PRozGOLJLgQmaJiIZnKgWEp7sQI5nY5sXRjXeLsKSlE9BrC7C+DuF3h3HRWuTFC62Q4hpYecZegohjFyLcixKMfNm+tqDHF5V/GPJTxu82hGTyPAxixBeVtNJXLxbi+/qVJR7psQPrami4isruQcAg8zFs3Hp/SwuoRs+0BPPEz7H7Clln1n9dGiE5nXq3gdB4fRM0idbS6E4a+JPJFNRVVyc+eKX6aN27phauDyr3AReX7Ucbz/fAK7mgHwbIvD1tzDm0FbMCWqb6QqEYjNCmD+zBxmIXLyaXvNdzOYoakyfhG+cGsxKchTlIM8q2rQ7rC0XQymyoL+91TaYFouUEndKQUL79bN8gD4wUm6Wel6TnJXyM3iBhwEFLnADYtzOAVbLf0fnUI6Y910kw+c40OERib7vcKX5JBkRQbpPrC5YzTjTd3mDYCOJyPrPEK7Nc7T3DkibGxI5nvqlo/Cnn";
        String s = new AesUtilWeiXin(apiVeKey.getBytes()).decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);
        System.out.println(s);
    }
}