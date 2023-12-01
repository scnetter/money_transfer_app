package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserService {

    public String API_BASE_URL = "http://localhost:8080/users";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User[] getUsers(){
        User[] users = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<User[]> response = restTemplate.exchange
                ("http://localhost:8080/users", HttpMethod.GET, entity, User[].class);

        users = response.getBody();
        return users;
    }
}
