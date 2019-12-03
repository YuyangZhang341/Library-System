package com.javateam019.view;


import org.jasypt.util.text.BasicTextEncryptor;

import org.jasypt.util.text.StrongTextEncryptor;

public class HashPaswd {
    public static void main(String[] args) {
        // 加密
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("akak");
        String newPassword = textEncryptor.encrypt("123456");
        System.out.println(newPassword);
        // 解密
        BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
        textEncryptor2.setPassword("akak");
        String oldPassword = textEncryptor2.decrypt(newPassword);
        System.out.println(oldPassword);
        System.out.println("--------------------------");
        /**
         * Utility class for easily performing high-strength encryption of
         * texts. This class internally holds a StandardPBEStringEncryptor
         * configured this way: Algorithm: PBEWithMD5AndTripleDES. Key obtention
         * iterations: 1000. The required steps to use it are: Create an
         * instance (using new). Set a password (using setPassword(String)).
         * Perform the desired encrypt(String) or decrypt(String) operations. To
         * use this class, you may need to download and install the Java
         * Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy
         * Files. This class is thread-safe.
         */
        StrongTextEncryptor ste = new StrongTextEncryptor();
        // 加密
        ste.setPassword("ahha");
        String encyptedResult = ste.encrypt("123456");
       // System.out.println("encyptedResult:" + encyptedResult);
        // 解密
        String dencyptedResult = ste.decrypt(encyptedResult);
        //System.out.println(dencyptedResult);


    }
}
