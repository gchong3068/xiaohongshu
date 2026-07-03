package com.gchong3068.framework.common.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Jasypt 加密解密工具类
 * @Author: gchong3068
 * @Date: 2024/11/6 17:23
 * @Version: v1.0.0
 **/
public class JasyptUtils {
    
    public static void main(String[] args) {
        Map<String ,String > value = new HashMap<>();
        value.put("qrJef5krmCmFJ/SfD8nW2WXoptsNBgj1EeGrClU/4Y5TJF2/+uNYBw==",EncryptOrDecrypt("qrJef5krmCmFJ/SfD8nW2WXoptsNBgj1EeGrClU/4Y5TJF2/+uNYBw==",false));
        value.put("oSm9dofd0SRlfEoLVj2JsUI60R4hp748UZzbZqLdWQ/nxeU5r4bi2zrWZ3PEXnyh",EncryptOrDecrypt("oSm9dofd0SRlfEoLVj2JsUI60R4hp748UZzbZqLdWQ/nxeU5r4bi2zrWZ3PEXnyh",false));
        value.put("4a6fXG74rirVTUNsUyI/kj/5m5dfwRLzVHL8GiBeanGpxrzV2SACtUb5+jDOJBKW",EncryptOrDecrypt("4a6fXG74rirVTUNsUyI/kj/5m5dfwRLzVHL8GiBeanGpxrzV2SACtUb5+jDOJBKW",false));
        for (Map.Entry<String, String> entry : value.entrySet()){
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // String password = "qrJef5krmCmFJ/SfD8nW2WXoptsNBgj1EeGrClU/4Y5TJF2/+uNYBw==";
        // System.out.println("password = " + EncryptOrDecrypt(password,false));
        // System.out.println("password = " + EncryptOrDecrypt("qrJef5krmCmFJ/SfD8nW2WXoptsNBgj1EeGrClU/4Y5TJF2/+uNYBw==",false));

    }

    /**
     * 加解密
     * @Auther: gchong3068
     * @Date: 2024/11/6 17:37 
     * @param value 加解密内容
     * @param isEnc true 加密，false 解密
     * @return java.lang.String
     **/
    public static String EncryptOrDecrypt(String value,boolean isEnc) {
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("8m8Jx6I7RqQQukf");  // 设置加密和解密时使用的密码
        config.setAlgorithm("PBEWithMD5AndDES");  // 设置加密算法，使用 PBEWithMD5AndDES
        config.setKeyObtentionIterations("1000");// 设置密钥获取的迭代次数，增加安全性
        config.setPoolSize("1");  // 设置加密器池的大小
        config.setProviderName("SunJCE");  // 设置加密提供者，使用 SunJCE 提供者
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");  // 设置盐生成器的类名
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");  // 设置初始化向量（IV）生成器的类名
        config.setStringOutputType("base64");  // 设置输出字符串的编码类型
        pooledPBEStringEncryptor.setConfig(config);

        if (isEnc){
            return pooledPBEStringEncryptor.encrypt(value);
        }
        return pooledPBEStringEncryptor.decrypt(value);
    }
}
