package com.frontend.niis_back.repository;

import com.frontend.niis_back.entity.Restaurant;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class RestaurantRepository {

    private final Firestore firestore;
    private static final String RESTAURANT_COLLECTION = "restaurant";

    @Autowired
    public RestaurantRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public QuerySnapshot getAllRestaurant() throws ExecutionException, InterruptedException {
        return firestore.collection(RESTAURANT_COLLECTION).get().get();
    }

    public String createRestaurant(Restaurant restaurant) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> future = firestore.collection(RESTAURANT_COLLECTION).add(restaurant);
        return future.get().getId();
    }

    public void updateRestaurant(String id, Restaurant restaurant) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(RESTAURANT_COLLECTION).document(id);
        Map<String, Object> restaurantMap = new HashMap<>();
        restaurantMap.put("name", restaurant.getName());
        restaurantMap.put("zipCode", restaurant.getZipCode());
        restaurantMap.put("location", restaurant.getLocation());

        ApiFuture<WriteResult> writeResult = docRef.set(restaurantMap, SetOptions.merge());
        writeResult.get();
    }

    public void deleteRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        firestore.collection(RESTAURANT_COLLECTION).document(restaurantId).delete().get();
    }
}
