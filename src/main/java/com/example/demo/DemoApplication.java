package com.example.demo;

import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;
import java.util.Map;

@SpringBootApplication
public class DemoApplication {
	static String token;
	public static void main(String[] args) throws Exception {
		token = GatewayTokenService.getToken();
		int output = BridgeService.registerCallbackUrl(token);
		if (output == 200 || output == 201) {
			String encryptedAdhaar = RSAPublicKeyEncryption.encryptTextUsingPublicKey("581403143354");
			Map<String, Object> result = AadhaarOtpRequester.sendEncryptedAadhaar(encryptedAdhaar, token);
			for (Map.Entry<String, Object> entry : result.entrySet()) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		} else {
			System.out.println("Failed");
		}
	}
}

