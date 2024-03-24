package com.example.demo;

import java.io.IOException;

public class DemoApplication2 {
    public static void main(String[] args) throws Exception {
        String aadhaarOtp = "";
        RSAPublicKeyEncryption encrypter = new RSAPublicKeyEncryption();
        String encryptedAadhaarOtp = encrypter.encryptTextUsingPublicKey(aadhaarOtp);

    }
}
