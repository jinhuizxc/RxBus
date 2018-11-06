package com.jh.rxbus.utils;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * Created by panda on 2017/7/4.
 */

public class SecurityUtil {
    /** 指定key的大小 */
    private static int KEYSIZE = 1024;
    /**
     * 生成密钥对
     */
    public static Map<String, String> generateKeyPair() throws Exception {
        /** RSA算法要求有一个可信任的随机数源 */
        SecureRandom sr = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象 */
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
        kpg.initialize(KEYSIZE, sr);
        /** 生成密匙对 */
        KeyPair kp = kpg.generateKeyPair();
        /** 得到公钥 */
        Key publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String pub = new String(Base64.encodeBase64(publicKeyBytes),
                ConfigureEncryptAndDecrypt.CHAR_ENCODING);
        /** 得到私钥 */
        Key privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String pri = new String(Base64.encodeBase64(privateKeyBytes),
                ConfigureEncryptAndDecrypt.CHAR_ENCODING);

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", pub);
        map.put("privateKey", pri);
        RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
        BigInteger bint = rsp.getModulus();
        byte[] b = bint.toByteArray();
        byte[] deBase64Value = Base64.encodeBase64(b);
        String retValue = new String(deBase64Value);
        map.put("modulus", retValue);
        return map;
    }

    /**
     * 加密方法 source： 源数据
     */
    public static String encrypt(String source, String publicKey)
            throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] b = source.getBytes();
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return new String(Base64.encodeBase64(b1),
                ConfigureEncryptAndDecrypt.CHAR_ENCODING);
    }

    /**
     * 加密方法 source： 源数据
     */
    public static String encrypt(byte[] source, String publicKey)
            throws Exception {
        Key key = getPublicKey(publicKey);
        /** 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        /** 执行加密操作 */
        byte[] b1 = cipher.doFinal(source);
        return new String(Base64.encodeBase64(b1),
                ConfigureEncryptAndDecrypt.CHAR_ENCODING);
    }

    /**
     * 多包数据加密
     * @param source           加密内容
     * @param publicKey        公钥
     * @return
     * @throws Exception
     */
    public static String packageEncrypt(String source, String publicKey) throws Exception {
        byte[] b = source.getBytes();
        String miStr = "";
        int index = 0;
        int len = 0;
        while (b.length > 117 && index < b.length){
            len = b.length - index;
            len = len > 117?117:len;
            byte[] dest = new byte[len];
            System.arraycopy(b, index, dest, 0, len);
            miStr = miStr + encrypt(dest, publicKey);
            index += len;
        }
        len = b.length - index;
        if (len > 0){
            byte[] dest = new byte[len];
            System.arraycopy(b, index, dest, 0, len);
            miStr = miStr + encrypt(dest, publicKey);
        }
        return miStr;
    }

    /**
     * 解密算法 cryptograph:密文
     */
    public static String decrypt(String cryptograph, String privateKey)
            throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return new String(b);
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     */
    public static byte[] decryptByByte(String cryptograph, String privateKey)
            throws Exception {
        Key key = getPrivateKey(privateKey);
        /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
        /** 执行解密操作 */
        byte[] b = cipher.doFinal(b1);
        return b;
    }

    public static String packageDecrypt(String cryptograph, String privateKey) throws Exception {
        String minStr = "";
        List<byte[]> lst= new ArrayList<byte[]>();
        int index = 0;
        int len = 0;
        while (cryptograph.length() > 172 && index < cryptograph.length()){
            len = cryptograph.length() - index;
            len = len > 172?172:len;
            lst.add(decryptByByte(cryptograph.substring(index, index + len), privateKey));
            index += len;
        }
        len = cryptograph.length() - index;
        if (len > 0){
            lst.add(decryptByByte(cryptograph.substring(index, index + len), privateKey));
        }
        byte[] bmin = new byte[(lst.size() - 1) * 117 + lst.get(lst.size() - 1).length];
        for (int i = 0; i < lst.size(); i ++){
            System.arraycopy(lst.get(i), 0, bmin, i * 117, lst.get(i).length);
        }
        return new String(bmin);
    }
    /**
     * 得到公钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(key.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String sign(String content, String privateKey) {
        String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(privateKey.getBytes()));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {

        }

        return null;
    }

    public static boolean checkSign(String content, String sign, String publicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode2(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = Signature
                    .getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update( content.getBytes("utf-8") );

            boolean bverify = signature.verify( Base64.decode2(sign) );
            return bverify;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args){
        //String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfdGUAyigve7S59ASQjlwGn+yFiHOMOzUuhxitT2VkKkMjGHRykGaLVEAEsmSWebolh/NQmgkZ/qfiXqYk4MHA5dD3bEysFn//tgDDh7cB/Pxya6IZy0Ge00BgWHo2SioprUyc778RawYXRX+tXvhxdJkfcDmRzH47ZflmL0RIsQIDAQAB";
        //String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAN90ZQDKKC97tLn0BJCOXAaf7IWIc4w7NS6HGK1PZWQqQyMYdHKQZotUQASyZJZ5uiWH81CaCRn+p+JepiTgwcDl0PdsTKwWf/+2AMOHtwH8/HJrohnLQZ7TQGBYejZKKimtTJzvvxFrBhdFf61e+HF0mR9wOZHMfjtl+WYvREixAgMBAAECgYBSd8eLChDYrWQeuNMycoe+HimRgrF6UKOo/0z5MXMvC3Xpru3TFj3yL+7MjPYYnuY3UbxXw/GV+WkGX34b4CNBGCKW8CwMbGPU5EFaModtWRUOBugTki/zRWjJH2GvZ1SPYDFNYvvlZfVuBAeVpQ23QQiesJ7DztsTnTJyw0HrDQJBAPjAtW2Zj7xmrLYXuGYYh1Y6waJnDzmlQiDUd/nCbAiP+JMZMQPCR/c48DAliRwr4K9f0O008AXyDzTK0iYRIc8CQQDl9wDMfQTp9OaorDuDUK/B7Aa1gCaqw4+lPlu2/f9QjuQYJqPrd0CsnoIlHcBsjMgpAJ7iuecotOVJIiG1Yw1/AkEA5XDSp5oJsyw9R+QPKNSdGFMvwX0v1OryWNfBJfVHsoohLIUC6cWsnYK+QJqOohZyszA8xF6cuc8waXBeb9BDjwJAPm82P05+BAriiM7iBX/lh+/Nzn8xgFFB8aohemgxZiy1Tr1G6cnhvDv4t50BPjdmrIMnoAS69rlOOvHSVw3CfwJAchhtGYRJS/5HqNGVgN7V+sSom2VJyA3VW2ypLFwqwxny8DEr0i9eNuYclO9R9LGE/22Yr/2RSJfeMtDZCKauNg==";
        //学生端App秘钥
//        ---publicKey--MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLZoWu7gHtkLludkkjrBtUTemqQY6kI5HxoqAjRKkupKEeFKBwXGF72Qte2BGlUakRHJtDLHc80JOQO2SDvaW5PVDL/FZ6a4kRJxmAbhm8q/ExzYqeRPJOY8kl42BNSV22Y6a9pS/W/8Ig8eYO0cO5z24Gpdki0/oqXlQQG0q18wIDAQAB
//                ---privateKey--MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAItmha7uAe2QuW52SSOsG1RN6apBjqQjkfGioCNEqS6koR4UoHBcYXvZC17YEaVRqREcm0MsdzzQk5A7ZIO9pbk9UMv8VnpriREnGYBuGbyr8THNip5E8k5jySXjYE1JXbZjpr2lL9b/wiDx5g7Rw7nPbgal2SLT+ipeVBAbSrXzAgMBAAECgYAL79+Ktz7bBQWb++0PbPF6KqHTvoFkdNdOhcqIupxyLg7N8J48gOyoGlHq8T2xlmiP2o9BFVGwl3vLYgqdbWP/dFPbuO3ZHvvn+8XLnKa6IyCCcYdlwgK4f7zLr0x09P1Bm9AezdmdNkdKuigxfuDSmDvFcYAcojbzb59CKIl+gQJBAM3LUddT5W6FI3I/40Fh9cKQJFZFycLec4lSbDSaewIQMS9o5xgqETkB59RvXUuft/GnFXwKsARRnBSkPkde83ECQQCtaKUrsI+oq0wOXBr291SabtkwBeltb+LFDmGMGQOpL6jrLR1CAofs3QHDAnp2KQyVaaAXH7sk45l7vfiFHIWjAkEAuE5tf5Ftmyu18S5yky3uck+xm2ppJhMgGk4tBneLzu89fZ5PyX5zakDgpYsPXRkwHkZroWnY9iU4yevGsxjAoQJBAKYWXmy0FURlnNj8Gs+EIMIxfU/juamZykcW+RMoOjtnbJCjiyxYJDwXicJvsz9NHcJVgoHjYGl6nNzKHSfRI6UCQQCp5LMxTNQdWlvSeKnKyf0XCYKgBpuD274NBiXH67attth18MJ9i1w6qntCgPQB7pabo9P/lLqU8U8AeBR3BE+7
        String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLZoWu7gHtkLludkkjrBtUTemqQY6kI5HxoqAjRKkupKEeFKBwXGF72Qte2BGlUakRHJtDLHc80JOQO2SDvaW5PVDL/FZ6a4kRJxmAbhm8q/ExzYqeRPJOY8kl42BNSV22Y6a9pS/W/8Ig8eYO0cO5z24Gpdki0/oqXlQQG0q18wIDAQAB";
        String privateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAItmha7uAe2QuW52SSOsG1RN6apBjqQjkfGioCNEqS6koR4UoHBcYXvZC17YEaVRqREcm0MsdzzQk5A7ZIO9pbk9UMv8VnpriREnGYBuGbyr8THNip5E8k5jySXjYE1JXbZjpr2lL9b/wiDx5g7Rw7nPbgal2SLT+ipeVBAbSrXzAgMBAAECgYAL79+Ktz7bBQWb++0PbPF6KqHTvoFkdNdOhcqIupxyLg7N8J48gOyoGlHq8T2xlmiP2o9BFVGwl3vLYgqdbWP/dFPbuO3ZHvvn+8XLnKa6IyCCcYdlwgK4f7zLr0x09P1Bm9AezdmdNkdKuigxfuDSmDvFcYAcojbzb59CKIl+gQJBAM3LUddT5W6FI3I/40Fh9cKQJFZFycLec4lSbDSaewIQMS9o5xgqETkB59RvXUuft/GnFXwKsARRnBSkPkde83ECQQCtaKUrsI+oq0wOXBr291SabtkwBeltb+LFDmGMGQOpL6jrLR1CAofs3QHDAnp2KQyVaaAXH7sk45l7vfiFHIWjAkEAuE5tf5Ftmyu18S5yky3uck+xm2ppJhMgGk4tBneLzu89fZ5PyX5zakDgpYsPXRkwHkZroWnY9iU4yevGsxjAoQJBAKYWXmy0FURlnNj8Gs+EIMIxfU/juamZykcW+RMoOjtnbJCjiyxYJDwXicJvsz9NHcJVgoHjYGl6nNzKHSfRI6UCQQCp5LMxTNQdWlvSeKnKyf0XCYKgBpuD274NBiXH67attth18MJ9i1w6qntCgPQB7pabo9P/lLqU8U8AeBR3BE+7";

        try {
//            Map<String,String> map = new HashMap<String,String>();
//            map =  generateKeyPair();
//            JSONObject
            JSONObject json = new JSONObject();
            //json.put("agencyNo", "TY000001");
            json.put("password", "632017");
            String data =null;
            data =packageEncrypt("123456", publicKey);
//XMSNtp3zzt5Y9VTAJ3OCtNBjKkLNmA6lUcXl1jopcpBmM4f3oZwYAtMD8JncU1s9rNX1FPtyjCxnsKCDVDiubo/fU7iaCSJ3EOD/L1HmkyCp/U+vnlXdUmFQP1/B6GkSiIa6VpkwFQAkupHVgc5mAjSpErreSmXB1plFzG+LfgM=
            System.out.println(data);
            System.out.println("解密"+packageDecrypt(data, privateKey));

            // System.out.println("---publicKey--"+map.get("publicKey"));
            // System.out.println("---privateKey--"+map.get("privateKey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
