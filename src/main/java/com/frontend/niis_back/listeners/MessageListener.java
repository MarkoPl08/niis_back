package com.frontend.niis_back.listeners;

import com.frontend.niis_back.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @JmsListener(destination = "restaurant.get.queue")
    public void handleGetRestaurantMessage(String message) {
        logger.info("Received restaurant get message: {}", message);
        FileUtil.appendToFile("restaurant-get-messages.txt", message);
    }

    @JmsListener(destination = "restaurant.create.queue")
    public void handleCreateRestaurantMessage(String message) {
        logger.info("Received restaurant creation message: {}", message);
        FileUtil.appendToFile("restaurant-creation-messages.txt", message);
    }

    @JmsListener(destination = "restaurant.update.queue")
    public void handleUpdateRestaurantMessage(String message) {
        logger.info("Received restaurant update message: {}", message);
        FileUtil.appendToFile("restaurant-update-messages.txt", message);
    }

    @JmsListener(destination = "restaurant.delete.queue")
    public void handleDeleteRestaurantMessage(String message) {
        logger.info("Received restaurant deletion message: {}", message);
        FileUtil.appendToFile("restaurant-deletion-messages.txt", message);
    }

    @JmsListener(destination = "restaurant.error.queue")
    public void handleRestaurantErrorMessage(String message) {
        logger.error("Received restaurant error message: {}", message);
        FileUtil.appendToFile("restaurant-error-messages.txt", message);
    }

    @JmsListener(destination = "reviews.get.queue")
    public void handleGetReviewsMessage(String message) {
        logger.info("Received get reviews message: {}", message);
        FileUtil.appendToFile("review-get-messages.txt", message);
    }

    @JmsListener(destination = "reviews.create.queue")
    public void handleCreateReviewMessage(String message) {
        logger.info("Received create review message: {}", message);
        FileUtil.appendToFile("review-create-messages.txt", message);
    }

    @JmsListener(destination = "reviews.update.queue")
    public void handleUpdateReviewMessage(String message) {
        logger.info("Received update review message: {}", message);
        FileUtil.appendToFile("review-update-messages.txt", message);
    }

    @JmsListener(destination = "reviews.delete.queue")
    public void handleDeleteReviewMessage(String message) {
        logger.info("Received delete review message: {}", message);
        FileUtil.appendToFile("review-delete-messages.txt", message);
    }

    @JmsListener(destination = "reviews.error.queue")
    public void handleReviewErrorMessage(String message) {
        logger.error("Received review error message: {}", message);
        FileUtil.appendToFile("review-error-messages.txt", message);
    }
}