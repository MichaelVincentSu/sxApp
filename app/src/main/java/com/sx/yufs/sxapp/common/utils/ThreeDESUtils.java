package com.sx.yufs.sxapp.common.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


/**
 * 3des加密解密工具类
 * Created by yufs on 2016/8/1.
 */
public class ThreeDESUtils {

    //定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede/CBC/PKCS7Padding";
    private static final String PASSWORD_CRYPT_KEY = "019121729545445101912172";


    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @return
     */
    public static String encryptMode(String src) {
        try {
            //生成密钥
            DESedeKeySpec deskey = new DESedeKeySpec(PASSWORD_CRYPT_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(deskey);
            //iv
            IvParameterSpec iv = new IvParameterSpec("95454451".getBytes("UTF-8"));
            AlgorithmParameterSpec paramSpec = iv;
            //实例化负责加密/解密的Cipher工具类
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, securekey, paramSpec);    //初始化为加密模式
            byte[] bytes = c1.doFinal(src.getBytes());

            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


//    /**
//     * 解密函数
//     * @param src 密文的字节数组
//     * @return
//     */
//    public static byte[] decryptMode(byte[] src) {
//        try {
//            SecretKey deskey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
//            Cipher c1 = Cipher.getInstance(Algorithm);
//            c1.init(Cipher.DECRYPT_MODE, deskey);    //初始化为解密模式
//            return c1.doFinal(src);
//        } catch (java.security.NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        } catch (javax.crypto.NoSuchPaddingException e2) {
//            e2.printStackTrace();
//        } catch (java.lang.Exception e3) {
//            e3.printStackTrace();
//        }
//        return null;
//    }


    /*
     * 根据字符串生成密钥字节数组
     * @param keyStr 密钥字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8");    //将字符串转成字节数组

        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if (key.length > temp.length) {
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
