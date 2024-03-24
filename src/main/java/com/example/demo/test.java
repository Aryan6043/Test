package com.example.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class test {

    public static void main(String[] args) throws Exception {
        // The URL from which to fetch the public key
        String url = "https://healthidsbx.abdm.gov.in/api/v2/auth/cert";

        // Use HttpClient to fetch the public key
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET() // Explicitly stating GET request
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Extracting the public key from response
        String publicKeyStr = response.body();
        System.out.println("Fetched Public Key: \n" + publicKeyStr);

        // Processing the public key
        publicKeyStr = publicKeyStr.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] decodedKey = Base64.getDecoder().decode(publicKeyStr);

        // Generate PublicKey object
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(spec);

        // The string to encrypt
        String stringToEncrypt = "251412547896";

        // Encrypt the string
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(stringToEncrypt.getBytes());

        // Convert encrypted bytes to Base64 to get a readable form
        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        System.out.println("Encrypted String: " + encryptedString);
    }
}

