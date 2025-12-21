package com.ecommerce.user.services;

import com.ecommerce.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakAdminService {

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAdminAccessToken ()
    {
        LinkedMultiValueMap<Object, Object> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("username", adminUsername);
        params.add("password", adminPassword);
        params.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<LinkedMultiValueMap<Object, Object>> entity =
                new HttpEntity<>(params, headers);

        String url = keycloakServerUrl
                + "/realms/master"
                + "/protocol/openid-connect/token";

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                entity,
                Map.class
        );
        return (String) response.getBody().get("access_token");
    }

    public String createUser (String token, UserRequest userRequest)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", userRequest.getUsername());
        userPayload.put("email", userRequest.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("firstName", userRequest.getFirstName());
        userPayload.put("lastName", userRequest.getLastName());


        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", userRequest.getPassword());
        credential.put("temporary", false);

        userPayload.put("credentials", List.of(credential));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(userPayload, headers);

        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";

        ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                entity,
                String.class
        );

        if (!HttpStatus.CREATED.equals(response.getStatusCode()))
        {
            throw new RuntimeException("Failed to create user in keycloak "+ response.getBody());
        }

        // Extract Keycloak user id
        URI location = response.getHeaders().getLocation();

        if (location == null)
        {
            throw new RuntimeException("Keycloak did not return the Location Header " + response.getBody());
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
