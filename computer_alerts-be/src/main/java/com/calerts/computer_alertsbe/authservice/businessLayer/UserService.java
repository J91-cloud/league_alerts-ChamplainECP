package com.calerts.computer_alertsbe.authservice.businessLayer;

import com.calerts.computer_alertsbe.AuthDomain.presentationLayer.UserRequestDTO;
import com.calerts.computer_alertsbe.authservice.presentationlayer.UserResponseModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class UserService {
   private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth0.domain}")
    private String AUTH0_DOMAIN;

    @Value("${auth0.clientId}")
    private String CLIENT_ID;

    @Value("${auth0.clientSecret}")
    private String CLIENT_SECRET;

    @Value("${auth0.audience}")
    private String AUDIENCE;

    // Retrieves the Management API token
    private String getManagementApiToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "client_credentials");
            map.add("client_id", CLIENT_ID);
            map.add("client_secret", CLIENT_SECRET);
            map.add("audience", AUDIENCE);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://" + AUTH0_DOMAIN + "/oauth/token",
                    request,
                    Map.class
            );

            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            System.err.println("Failed to retrieve Management API token: " + e.getMessage());
            throw new RuntimeException("Could not obtain Management API token", e);
        }
    }


    public UserResponseModel createUser(UserRequestDTO request) {
        String url = "https://" + AUTH0_DOMAIN + "/api/v2/users";

        String managementApiToken = getManagementApiToken();
        if (managementApiToken == null || managementApiToken.isEmpty()) {
            throw new RuntimeException("Management API token is missing or invalid");
        }

        // Add headers with Management API token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + managementApiToken);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", request.getEmail());
        requestBody.put("password", request.getPassword());
        requestBody.put("connection", "Username-Password-Authentication");

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                String userId = (String) responseBody.get("user_id");

                return new UserResponseModel(
                        request.getEmail(),
                        userId // Include user ID in response model
                );
            } else {
                throw new RuntimeException("Failed to create user: " + response.getBody());
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error communicating with Auth0: " + e.getMessage(), e);
        }
    }
}

