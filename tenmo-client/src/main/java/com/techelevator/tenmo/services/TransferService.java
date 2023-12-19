package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.web.client.RestTemplate;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {

    public String API_BASE_URL = "http://localhost:8080/transfers";
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean sendBucks(TransferDto transferDto){
        boolean status = false;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransferDto> entity = new HttpEntity(transferDto, headers);
        try {
            restTemplate.exchange(API_BASE_URL, HttpMethod.POST, entity, Account.class);
            status = true;
        }catch (RestClientResponseException ex){
            System.out.println(ex.getRawStatusCode());
        }
        return status;
    }
}
