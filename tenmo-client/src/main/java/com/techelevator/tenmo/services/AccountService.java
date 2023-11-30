package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
public class AccountService {

    public String API_BASE_URL = "http://localhost:8080/accounts";
    private final RestTemplate restTemplate = new RestTemplate();
    public Account getAccountById(int accountId) {
        Account account = null;
        account = restTemplate.getForObject(API_BASE_URL + "/" + accountId, Account.class);
        return account;
    }


}
