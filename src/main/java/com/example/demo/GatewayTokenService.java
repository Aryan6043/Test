package com.example.demo;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GatewayTokenService {
    public static String getToken() throws JSONException {
        // Creating the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Creating the request body
        JSONObject requestJson = new JSONObject();
        requestJson.put("clientId", "SBX_002928");
        requestJson.put("clientSecret", "5b24ab9e-2194-4f5f-aca3-fdb0a4872312");
        // Assuming "grantType" is required and is client_credentials
        requestJson.put("grantType", "client_credentials");

        // Wrapping the request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson.toString(), headers);

        // Creating an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Sending the POST request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://dev.abdm.gov.in/gateway/v0.5/sessions",
                HttpMethod.POST,
                requestEntity,
                String.class);

        // Extracting the access token from the response
        JSONObject responseJson = new JSONObject(responseEntity.getBody());
        String accessToken = responseJson.getString("accessToken");
        return accessToken;
    }
}

