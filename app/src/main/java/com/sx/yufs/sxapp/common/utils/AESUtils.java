package com.sx.yufs.sxapp.common.utils;

import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密工具类
 */
public class AESUtils {
    /**
     * Logcat Tag
     */
    private static final String TAG = "AESUtils";

    /**
     * 密钥
     */
    private static final String KEY = "sGKKvP4sh8wF4B6b";

    /**
     * 算法/模式/填充
     */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    /**
     * AES字符串加密处理
     *
     * @param data 要加密的字符串
     * @return 加密后的Base64编码字符串
     */
    public static String encrypt(String data) {
        Log.d(TAG, "加密前的内容为:" + data);
        String content = null;
        try {
            byte[] rawkey = getRawKey(Base64.decode(KEY, Base64.DEFAULT));
            byte[] result = encrypt(rawkey, data.getBytes("UTF-8"));
            content = Base64.encodeToString(result, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "加密后的内容为:" + content);

        return content;
    }

    /**
     * AES字符串解密处理
     *
     * @param data 要解密的字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String data) {
        Log.d(TAG, "解密前的内容为:" + data);
        String content = null;
        try {
            byte[] rawKey = getRawKey(Base64.decode(KEY, Base64.DEFAULT));
            byte[] enc = Base64.decode(data, Base64.DEFAULT);
            byte[] result = decrypt(rawKey, enc);
            content = new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "解密后的内容为:" + content);

        return content;
    }

    private static byte[] getRawKey(byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = null;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        secureRandom.setSeed(key);
        kgen.init(128, secureRandom);
        SecretKey sKey = kgen.generateKey();
        byte[] encodedKey = sKey.getEncoded();

        return encodedKey;
    }

    private static byte[] encrypt(byte[] raw, byte[] data) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(data);
        return encrypted;
    }

    private static byte[] decrypt(byte[] key, byte[] data) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(data);
        return decrypted;
    }

}