package com.luxuryadmin.util;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author xy-peng
 */
public class AesUtil {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private static final int KEY_LENGTH_BYTE = 32;
    private static final int TAG_LENGTH_BIT = 128;

    private final byte[] aesKey;

    public AesUtil(byte[] key) {
        if (key.length != KEY_LENGTH_BYTE) {
            throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
        }
        this.aesKey = key;
    }

    public String decryptToString(byte[] associatedData, byte[] nonce, String ciphertext)
            throws GeneralSecurityException {
        try {
            SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static void main(String[] args) throws GeneralSecurityException, IOException {

        String apiVeKey = "43f71e0c8a08459197427588c425125d";
        String associated_data = "transaction";
        String nonce = "QtyQzK25QLpT";
        String ciphertext = "vTxP1bMugX4f6Yx4+f7CPKlPoV5IEDSL2bqasmnO+t4yva8s0e9z1ZzUtQzT+HJ6wDMTOHssGUnnvky1hjGQ+FxcM48WIkt7MUecJrGlK2M9crjJVh2rE+PRozGOLJLgQmaJiIZnKgWEp7sQI5nY5sXRjXeLsKSlE9BrC7C+DuF3h3HRWuTFC62Q4hpYecZegohjFyLcixKMfNm+tqDHF5V/GPJTxu82hGTyPAxixBeVtNJXLxbi+/qVJR7psQPrami4isruQcAg8zFs3Hp/SwuoRs+0BPPEz7H7Clln1n9dGiE5nXq3gdB4fRM0idbS6E4a+JPJFNRVVyc+eKX6aN27phauDyr3AReX7Ucbz/fAK7mgHwbIvD1tzDm0FbMCWqb6QqEYjNCmD+zBxmIXLyaXvNdzOYoakyfhG+cGsxKchTlIM8q2rQ7rC0XQymyoL+91TaYFouUEndKQUL79bN8gD4wUm6Wel6TnJXyM3iBhwEFLnADYtzOAVbLf0fnUI6Y910kw+c40OERib7vcKX5JBkRQbpPrC5YzTjTd3mDYCOJyPrPEK7Nc7T3DkibGxI5nvqlo/Cnn";
        AesUtil aesUtil = new AesUtil(apiVeKey.getBytes());
        String s = aesUtil.decryptToString(associated_data.getBytes(), nonce.getBytes(), ciphertext);
        System.out.println(s);
    }
}
