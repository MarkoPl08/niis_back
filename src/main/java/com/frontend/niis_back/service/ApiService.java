package com.frontend.niis_back.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl = "http://localhost:8080";

    @Autowired
    public ApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Restaurants
    public List<RestaurantDTO> getAllRestaurants() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/restaurant", String.class);
        System.out.println("Received JSON: " + response.getBody());
        return Arrays.asList(objectMapper.readValue(response.getBody(), RestaurantDTO[].class));
    }

    public RestaurantDTO createRestaurant(RestaurantDTO restaurantDTO) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/restaurant", restaurantDTO, String.class);
        System.out.println("Received JSON: " + response.getBody());
        return objectMapper.readValue(response.getBody(), RestaurantDTO.class);
    }

    public void updateRestaurant(String id, RestaurantDTO restaurantDTO) throws JsonProcessingException {
        restTemplate.put(baseUrl + "/restaurant/" + id, restaurantDTO);
        // Assuming PUT returns void; if it returns content, you can log and deserialize similarly
    }

    public void deleteRestaurant(String restaurantId) {
        restTemplate.delete(baseUrl + "/restaurant/" + restaurantId);
        // DELETE typically does not return content, if it does, handle similarly
    }

    // Reviews
    public List<ReviewDTO> getReviewsForRestaurant(String restaurantId) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/restaurant/" + restaurantId + "/reviews", String.class);
        System.out.println("Received JSON: " + response.getBody());
        return Arrays.asList(objectMapper.readValue(response.getBody(), ReviewDTO[].class));
    }

    public ReviewDTO createReview(String restaurantId, ReviewDTO reviewDTO) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/restaurant/" + restaurantId + "/reviews", reviewDTO, String.class);
        System.out.println("Received JSON: " + response.getBody());
        return objectMapper.readValue(response.getBody(), ReviewDTO.class);
    }

    public void updateReview(String restaurantId, String reviewId, ReviewDTO reviewDTO) throws JsonProcessingException {
        restTemplate.put(baseUrl + "/restaurant/" + restaurantId + "/reviews/" + reviewId, reviewDTO);
        // Assuming PUT returns void; if it returns content, you can log and deserialize similarly
    }

    public void deleteReview(String restaurantId, String reviewId) {
        restTemplate.delete(baseUrl + "/restaurant/" + restaurantId + "/reviews/" + reviewId);
        // DELETE typically does not return content, if it does, handle similarly
    }
}
