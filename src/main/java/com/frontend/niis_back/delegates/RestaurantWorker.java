package com.frontend.niis_back.delegates;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.service.ApiService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class RestaurantWorker {

    private final ApiService apiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RestaurantWorker(ApiService apiService, ObjectMapper objectMapper) {
        this.apiService = apiService;
        this.objectMapper = objectMapper;
    }

    @ZeebeWorker(type = "getAllRestaurants")
    public void getAllRestaurants(final JobClient client, final ActivatedJob job) {
        try {
            List<RestaurantDTO> restaurants = apiService.getAllRestaurants();

            String restaurantsJson = objectMapper.writeValueAsString(restaurants);

            Map<String, Object> variables = Collections.singletonMap("restaurants", restaurantsJson);

            client.newCompleteCommand(job.getKey())
                    .variables(variables)
                    .send()
                    .join();
        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "createRestaurant")
    public void createRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            System.out.println("Received Variables Map: " + variables);

            Object restaurantObj = variables.get("restaurant");
            if (restaurantObj instanceof Map) {
                Map<String, Object> restaurantMap = (Map<String, Object>) restaurantObj;
                String restaurantJson = objectMapper.writeValueAsString(restaurantMap);
                System.out.println("Received restaurant variable (JSON dump): " + restaurantJson);

                RestaurantDTO restaurantDTO = objectMapper.readValue(restaurantJson, RestaurantDTO.class);
                RestaurantDTO createdRestaurant = apiService.createRestaurant(restaurantDTO);

                client.newCompleteCommand(job.getKey())
                        .variables(Collections.singletonMap("createdRestaurantId", createdRestaurant.getId()))
                        .send()
                        .join();
            } else {
                throw new IllegalStateException("Expected a Map for 'restaurant', received type: " + (restaurantObj == null ? "null" : restaurantObj.getClass().getSimpleName()));
            }
        } catch (Exception e) {
            System.err.println("Exception in createRestaurant: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "updateRestaurant")
    public void updateRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantJson = objectMapper.writeValueAsString(variables.get("restaurant"));

            RestaurantDTO restaurantDTO = objectMapper.readValue(restaurantJson, RestaurantDTO.class);
            apiService.updateRestaurant(restaurantDTO.getId(), restaurantDTO);

            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            System.err.println("Exception in updateRestaurant: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }


    @ZeebeWorker(type = "deleteRestaurant")
    public void deleteRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            String restaurantId = job.getVariablesAsMap().get("restaurantId").toString();
            apiService.deleteRestaurant(restaurantId);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "getReviews")
    public void getReviews(final JobClient client, final ActivatedJob job) {
        try {
            String restaurantId = job.getVariablesAsMap().get("restaurantId").toString();
            List<ReviewDTO> reviews = apiService.getReviewsForRestaurant(restaurantId);

            String reviewsJson = objectMapper.writeValueAsString(reviews);

            client.newCompleteCommand(job.getKey())
                    .variables(Collections.singletonMap("reviews", reviewsJson))
                    .send()
                    .join();
        } catch (Exception e) {
            System.err.println("Exception in getReviews: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "createReview")
    public void createReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get("restaurantId").toString();
            ReviewDTO reviewDTO = objectMapper.convertValue(variables.get("review"), ReviewDTO.class);
            ReviewDTO createdReview = apiService.createReview(restaurantId, reviewDTO);

            client.newCompleteCommand(job.getKey())
                    .variables(Collections.singletonMap("createdReviewId", createdReview.getId()))
                    .send()
                    .join();
        } catch (Exception e) {
            System.err.println("Exception in createReview: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "updateReview")
    public void updateReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get("restaurantId").toString();
            String reviewId = variables.get("reviewId").toString();
            ReviewDTO reviewDTO = objectMapper.convertValue(variables.get("review"), ReviewDTO.class);

            apiService.updateReview(restaurantId, reviewId, reviewDTO);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            System.err.println("Exception in updateReview: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @ZeebeWorker(type = "deleteReview")
    public void deleteReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get("restaurantId").toString();
            String reviewId = variables.get("reviewId").toString();

            apiService.deleteReview(restaurantId, reviewId);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            System.err.println("Exception in deleteReview: " + e.getMessage());
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }


}
