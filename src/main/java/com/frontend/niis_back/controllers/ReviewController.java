package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.service.MessageService;
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
    private final MessageService messageService;

    @Autowired
    public ReviewController(ReviewService reviewService, MessageService messageService) {
        this.reviewService = reviewService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviewsForRestaurant(@PathVariable String restaurantId) {
        try {
            List<ReviewDTO> reviewList = reviewService.findReviewsForRestaurant(restaurantId);

            messageService.sendGetReviewsMessage("Fetched reviews for restaurant with ID: " + restaurantId);

            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();

            messageService.sendReviewErrorMessage("Error fetching reviews for restaurant: " + e.getMessage());

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@PathVariable String restaurantId, @RequestBody ReviewDTO reviewDTO) {
        try {
            String reviewId = reviewService.saveReview(restaurantId, reviewDTO);
            reviewDTO.setId(reviewId);

            messageService.sendCreateReviewMessage("Created review with ID: " + reviewId);

            return new ResponseEntity<>(reviewDTO, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();

            messageService.sendReviewErrorMessage("Error creating review: " + e.getMessage());

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

            messageService.sendUpdateReviewMessage("Updated review with ID: " + reviewId);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            messageService.sendReviewErrorMessage("Error updating review: " + e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String restaurantId, @PathVariable String reviewId) {
        try {
            reviewService.deleteReview(restaurantId, reviewId);

            messageService.sendDeleteReviewMessage("Deleted review with ID: " + reviewId);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            messageService.sendReviewErrorMessage("Error deleting review: " + e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
