package com.javateam019.view;


import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import org.jasypt.util.text.StrongTextEncryptor;

//some examples on hash and dehash
public class HashPaswd {
    public static void main(String[] args) {
        // hash
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("akak");
        String newPassword = textEncryptor.encrypt("123456");
        System.out.println(newPassword);
        // dehash
        BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
        textEncryptor2.setPassword("akak");
        String oldPassword = textEncryptor2.decrypt("123456");
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
        // hash
        ste.setPassword("ahha");
        String encyptedResult = ste.encrypt("123456");
       // System.out.println("encyptedResult:" + encyptedResult);
        // dehash
        String dencyptedResult = ste.decrypt(encyptedResult);
        //System.out.println(dencyptedResult);

        //strong huah and verifying
        String inputP= "123456";
        String userPassword = "123456";
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(userPassword);

        if (passwordEncryptor.checkPassword(inputP, encryptedPassword)) {
            //correct
            System.out.println(encryptedPassword);
            System.out.println("true");
        } else {
            // bad login!
            System.out.println("false");
        }

    }
}
