package com.frontend.niis_back.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private static final String LOG_RECEIVED_JSON = "Received JSON: {}";
    private static final String BASE_URL = "http://localhost:8080";
    private static final String RESTAURANT_PATH = "restaurant";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<RestaurantDTO> getAllRestaurants() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL +  "/" + RESTAURANT_PATH, String.class);
        logger.info(LOG_RECEIVED_JSON, response.getBody());
        return Arrays.asList(objectMapper.readValue(response.getBody(), RestaurantDTO[].class));
    }

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL +  "/" + RESTAURANT_PATH, restaurantDTO, String.class);
        logger.info(LOG_RECEIVED_JSON, response.getBody());
        return objectMapper.readValue(response.getBody(), RestaurantDTO.class);
    }

    public void updateRestaurant(String id, RestaurantDTO restaurantDTO) {
        restTemplate.put(BASE_URL + "/" + RESTAURANT_PATH + "/" + id, restaurantDTO);
    }

    public void deleteRestaurant(String restaurantId) {
        restTemplate.delete(BASE_URL +  "/" + RESTAURANT_PATH + "/" + restaurantId);
    }

    public List<ReviewDTO> getReviewsForRestaurant(String restaurantId) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL +  "/" + RESTAURANT_PATH + "/" + restaurantId + "/reviews", String.class);
        logger.info(LOG_RECEIVED_JSON, response.getBody());
        return Arrays.asList(objectMapper.readValue(response.getBody(), ReviewDTO[].class));
    }

    public ReviewDTO createReview(String restaurantId, ReviewDTO reviewDTO) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL +  "/" + RESTAURANT_PATH + "/" + restaurantId + "/reviews", reviewDTO, String.class);
        logger.info(LOG_RECEIVED_JSON, response.getBody());
        return objectMapper.readValue(response.getBody(), ReviewDTO.class);
    }

    public void updateReview(String restaurantId, String reviewId, ReviewDTO reviewDTO) {
        restTemplate.put(BASE_URL +  "/" + RESTAURANT_PATH + "/" + restaurantId + "/reviews/" + reviewId, reviewDTO);
    }

    public void deleteReview(String restaurantId, String reviewId) {
        restTemplate.delete(BASE_URL +  "/" + RESTAURANT_PATH + "/" + restaurantId + "/reviews/" + reviewId);
    }
}
