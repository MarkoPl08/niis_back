package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.ReviewDTO;
import com.frontend.niis_back.service.MessageService;
import com.frontend.niis_back.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

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

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable String restaurantId,
                                             @PathVariable String reviewId,
                                             @RequestBody ReviewDTO reviewDTO) {
        if (!reviewId.equals(reviewDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            reviewService.updateReview(restaurantId, reviewDTO);
            messageService.sendUpdateReviewMessage("Updated review with ID: " + reviewId);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread was interrupted while updating review with ID: {}", reviewId, e);
            messageService.sendReviewErrorMessage("Thread interruption occurred during review update.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("Error updating review with ID: {}", reviewId, e);
            messageService.sendReviewErrorMessage("Error updating review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String restaurantId, @PathVariable String reviewId) {
        try {
            reviewService.deleteReview(restaurantId, reviewId);
            messageService.sendDeleteReviewMessage("Deleted review with ID: " + reviewId);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread was interrupted while deleting review with ID: {}", reviewId, e);
            messageService.sendReviewErrorMessage("Thread interruption occurred during review deletion.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            logger.error("Error deleting review with ID: {}", reviewId, e);
            messageService.sendReviewErrorMessage("Error deleting review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
