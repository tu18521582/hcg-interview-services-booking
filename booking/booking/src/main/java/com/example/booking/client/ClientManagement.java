package com.example.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ClientManagement {

    private static ClientManagement instance = null;

    public static ClientManagement getInstance() {
        if (instance == null) {
            instance = new ClientManagement();
        }
        return instance;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    public void invokePutMethod(String url, Map<String, Object> queryParams) throws HttpClientErrorException {
        try {
            restTemplate.put(url, queryParams, Void.class);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode());
        }
    }

    public String invokePostMethod(String url, Map<String, Object> queryParams) throws HttpClientErrorException {
        String response = "";
        try {
            response = restTemplate.postForObject(url, queryParams, String.class);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode());
        }

        return response;
    }
}

