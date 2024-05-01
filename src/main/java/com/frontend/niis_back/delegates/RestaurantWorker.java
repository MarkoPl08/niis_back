package com.frontend.niis_back.delegates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.service.ApiService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class RestaurantWorker {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantWorker.class);
    private static final String RESTAURANT_ID = "restaurantId";
    private final ApiService apiService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RestaurantWorker(ApiService apiService, ObjectMapper objectMapper) {
        this.apiService = apiService;
        this.objectMapper = objectMapper;
    }

    @JobWorker(type = "getAllRestaurants")
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
            logger.error("Exception in getAllRestaurants", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "createRestaurant")
    public void createRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            logger.info("Received Variables Map: {}", variables);

            Object restaurantObj = variables.get("restaurant");
            if (restaurantObj instanceof Map) {
                Map<String, Object> restaurantMap = (Map<String, Object>) restaurantObj;
                String restaurantJson = objectMapper.writeValueAsString(restaurantMap);
                logger.info("Received restaurant variable (JSON dump): {}", restaurantJson);

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
            logger.error("Exception in createRestaurant", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "updateRestaurant")
    public void updateRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantJson = objectMapper.writeValueAsString(variables.get("restaurant"));

            RestaurantDTO restaurantDTO = objectMapper.readValue(restaurantJson, RestaurantDTO.class);
            apiService.updateRestaurant(restaurantDTO.getId(), restaurantDTO);

            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            logger.error("Exception in updateRestaurant", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }


    @JobWorker(type = "deleteRestaurant")
    public void deleteRestaurant(final JobClient client, final ActivatedJob job) {
        try {
            String restaurantId = job.getVariablesAsMap().get(RESTAURANT_ID).toString();
            apiService.deleteRestaurant(restaurantId);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            logger.error("Exception in deleteRestaurant", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "getReviews")
    public void getReviews(final JobClient client, final ActivatedJob job) {
        try {
            String restaurantId = job.getVariablesAsMap().get(RESTAURANT_ID).toString();
            List<ReviewDTO> reviews = apiService.getReviewsForRestaurant(restaurantId);

            String reviewsJson = objectMapper.writeValueAsString(reviews);

            client.newCompleteCommand(job.getKey())
                    .variables(Collections.singletonMap("reviews", reviewsJson))
                    .send()
                    .join();
        } catch (Exception e) {
            logger.error("Exception in getReviews", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "createReview")
    public void createReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get(RESTAURANT_ID).toString();
            ReviewDTO reviewDTO = objectMapper.convertValue(variables.get("review"), ReviewDTO.class);
            ReviewDTO createdReview = apiService.createReview(restaurantId, reviewDTO);

            client.newCompleteCommand(job.getKey())
                    .variables(Collections.singletonMap("createdReviewId", createdReview.getId()))
                    .send()
                    .join();
        } catch (Exception e) {
            logger.error("Exception in createReview", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "updateReview")
    public void updateReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get(RESTAURANT_ID).toString();
            String reviewId = variables.get("reviewId").toString();
            ReviewDTO reviewDTO = objectMapper.convertValue(variables.get("review"), ReviewDTO.class);

            apiService.updateReview(restaurantId, reviewId, reviewDTO);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            logger.error("Exception in updateReview: ", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "deleteReview")
    public void deleteReview(final JobClient client, final ActivatedJob job) {
        try {
            Map<String, Object> variables = job.getVariablesAsMap();
            String restaurantId = variables.get(RESTAURANT_ID).toString();
            String reviewId = variables.get("reviewId").toString();

            apiService.deleteReview(restaurantId, reviewId);
            client.newCompleteCommand(job.getKey()).send().join();
        } catch (Exception e) {
            logger.error("Exception in deleteReview: ", e);
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
