package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/restaurant/{restaurantId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviewsForRestaurant(@PathVariable String restaurantId) {
        try {
            List<ReviewDTO> reviewList = reviewService.findReviewsForRestaurant(restaurantId);
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@PathVariable String restaurantId, @RequestBody ReviewDTO reviewDTO) {
        try {
            String reviewId = reviewService.saveReview(restaurantId, reviewDTO);
            reviewDTO.setId(reviewId);
            return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable String restaurantId,
                                             @PathVariable String reviewId,
                                             @RequestBody ReviewDTO reviewDTO) {
        try {
            if (!reviewId.equals(reviewDTO.getId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            reviewService.updateReview(restaurantId, reviewDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String restaurantId, @PathVariable String reviewId) {
        try {
            reviewService.deleteReview(restaurantId, reviewId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
