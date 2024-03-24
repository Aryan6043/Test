package com.example.demo;

import javax.crypto.Cipher;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAPublicKeyEncryption {
    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";
    private String publicKeyStr; // Public Key as an instance attribute

    public String getPublicKeyStr() {
        return publicKeyStr;
    }

    public RSAPublicKeyEncryption() throws IOException, InterruptedException {
        this.publicKeyStr = fetchPublicKey(); // Fetch the public key during object construction
    }

    private String fetchPublicKey() throws IOException, InterruptedException {
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
        String fetchedPublicKeyStr = response.body();
        System.out.println("Fetched Public Key: \n" + fetchedPublicKeyStr);

        // Processing the public key
        return fetchedPublicKeyStr.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
    }

    public String encryptTextUsingPublicKey(String data) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(this.publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}

