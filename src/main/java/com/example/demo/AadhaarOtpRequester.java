package com.example.demo;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class AadhaarOtpRequester {

    public static Map<String, Object> sendEncryptedAadhaar(String plainTextAadhaar, String bearerToken) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Assume RSAPublicKeyEncryption.encryptTextUsingPublicKey and PublicKeyService.fetchPublicKey() are available
            String encryptedAadhaar = RSAPublicKeyEncryption.encryptTextUsingPublicKey(plainTextAadhaar);

            // Prepare the body of the request
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("aadhaar", encryptedAadhaar);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(bearerToken); // Include the Bearer token in the Authorization header

            // Create an HttpEntity object
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Make the POST request
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://healthidsbx.abdm.gov.in/api/v2/registration/aadhaar/generateOtp",
                    HttpMethod.POST,
                    entity,
                    Map.class);

            // Check if response is 200 OK
            if (response.getStatusCode().is2xxSuccessful()) {
                // Successfully received response
                result.put("txnId", response.getBody().get("txnId"));
                result.put("success", true);
            } else {
                // Response indicates failure
                result.put("success", false);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            result.put("success", false);
        }
        return result;
    }

//    public static void main(String[] args) {
//        // Example usage
//        Map<String, Object> response = sendEncryptedAadhaar("581403143354");
//        System.out.println("Success: " + response.get("success"));
//        if ((Boolean) response.get("success")) {
//            System.out.println("Transaction ID: " + response.get("txnId"));
//        }
//    }
}

