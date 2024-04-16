package com.frontend.niis_back.service;

import com.frontend.niis_back.adatpers.ReviewAdapter;
import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.entity.Review;
import com.frontend.niis_back.repository.ReviewRepository;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDTO> findReviewsForRestaurant(String restaurantId) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = reviewRepository.getReviewsForRestaurant(restaurantId);
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            reviewDTOs.add(ReviewAdapter.adapt(document));
        }
        return reviewDTOs;
    }

    public String saveReview(String restaurantId, ReviewDTO reviewDTO) throws ExecutionException, InterruptedException {
        Review review = new Review(null, reviewDTO.getTitle(),
                reviewDTO.getText(), reviewDTO.getRating());

        return reviewRepository.addReviewToRestaurant(restaurantId, review);
    }

    public void updateReview(String restaurantId, ReviewDTO reviewDTO) throws ExecutionException, InterruptedException {
        Review review = new Review(reviewDTO.getId(), reviewDTO.getTitle(),
                reviewDTO.getText(), reviewDTO.getRating());
        reviewRepository.updateReview(restaurantId, review);
    }

    public void deleteReview(String restaurantId, String reviewId) throws ExecutionException, InterruptedException {
        reviewRepository.deleteReview(restaurantId, reviewId);
    }
}
