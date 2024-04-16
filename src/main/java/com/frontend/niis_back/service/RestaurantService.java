package com.frontend.niis_back.service;

import com.frontend.niis_back.adatpers.RestaurantAdapter;
import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.entity.Restaurant;
import com.frontend.niis_back.repository.RestaurantRepository;
import com.frontend.niis_back.repository.ReviewRepository;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<RestaurantDTO> findAllRestaurant() throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = restaurantRepository.getAllRestaurant();
        List<RestaurantDTO> restaurantDTOs = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            restaurantDTOs.add(RestaurantAdapter.adapt(document));
        }
        return restaurantDTOs;
    }

    public String saveRestaurant(RestaurantDTO restaurantDTO) throws ExecutionException, InterruptedException {
        Restaurant restaurant = new Restaurant(restaurantDTO.getName(),
                restaurantDTO.getZipCode(), restaurantDTO.getLocation());
        return restaurantRepository.createRestaurant(restaurant);
    }

    public void updateRestaurant(RestaurantDTO restaurantDTO) throws ExecutionException, InterruptedException {
        Restaurant restaurant = new Restaurant(restaurantDTO.getName(), restaurantDTO.getZipCode(), restaurantDTO.getLocation());

        restaurant.setId(restaurantDTO.getId());

        restaurantRepository.updateRestaurant(restaurant);
    }

    public void deleteRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        reviewRepository.deleteAllReviewsForRestaurant(restaurantId);

        restaurantRepository.deleteRestaurant(restaurantId);
    }
}


