package com.frontend.niis_back.repository;

import com.frontend.niis_back.entity.Review;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class ReviewRepository {

    private final Firestore firestore;

    @Autowired
    public ReviewRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public QuerySnapshot getReviewsForRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        return firestore.collection("restaurant")
                .document(restaurantId)
                .collection("reviews")
                .get()
                .get();
    }

    public String addReviewToRestaurant(String restaurantId, Review review) throws ExecutionException, InterruptedException {
        DocumentReference restaurantRef = firestore.collection("restaurant").document(restaurantId);
        ApiFuture<DocumentReference> future = restaurantRef.collection("reviews").add(review);
        return future.get().getId();
    }

    public void updateReview(String restaurantId, Review review) throws ExecutionException, InterruptedException {
        DocumentReference reviewRef = firestore.collection("restaurant")
                .document(restaurantId)
                .collection("reviews")
                .document(review.getId());
        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("title", review.getTitle());
        reviewMap.put("text", review.getText());
        reviewMap.put("rating", review.getRating());

        ApiFuture<WriteResult> writeResult = reviewRef.set(reviewMap, SetOptions.merge());
        writeResult.get();
    }

    public void deleteAllReviewsForRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection("restaurant")
                .document(restaurantId)
                .collection("reviews")
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (DocumentSnapshot document : documents) {
            firestore.collection("restaurant")
                    .document(restaurantId)
                    .collection("reviews")
                    .document(document.getId())
                    .delete()
                    .get();
        }
    }

    public void deleteReview(String restaurantId, String reviewId) throws ExecutionException, InterruptedException {
        firestore.collection("restaurant")
                .document(restaurantId)
                .collection("reviews")
                .document(reviewId)
                .delete()
                .get();
    }
}
