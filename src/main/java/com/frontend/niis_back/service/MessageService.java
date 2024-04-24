package com.frontend.niis_back.service;

import com.frontend.niis_back.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final JmsTemplate jmsTemplate;

    @Autowired
    public MessageService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendGetRestaurantMessage(String message) {
        jmsTemplate.convertAndSend("restaurant.get.queue", message);
        FileUtil.appendToFile("restaurant_get.txt", message);
    }

    public void sendCreateRestaurantMessage(String message) {
        jmsTemplate.convertAndSend("restaurant.create.queue", message);
        FileUtil.appendToFile("restaurant_save.txt", message);
    }

    public void sendUpdateRestaurantMessage(String message) {
        jmsTemplate.convertAndSend("restaurant.update.queue", message);
        FileUtil.appendToFile("restaurant_update.txt", message);
    }

    public void sendDeleteRestaurantMessage(String message) {
        jmsTemplate.convertAndSend("restaurant.delete.queue", message);
        FileUtil.appendToFile("restaurant_delete.txt", message);
    }

    public void sendRestaurantErrorMessage(String message) {
        jmsTemplate.convertAndSend("restaurant.error.queue", message);
        FileUtil.appendToFile("restaurant_error.txt", message);
    }

    public void sendGetReviewsMessage(String message) {
        jmsTemplate.convertAndSend("reviews.get.queue", message);
        FileUtil.appendToFile("review_get.txt", message);
    }

    public void sendCreateReviewMessage(String message) {
        jmsTemplate.convertAndSend("reviews.create.queue", message);
        FileUtil.appendToFile("review_create.txt", message);
    }

    public void sendUpdateReviewMessage(String message) {
        jmsTemplate.convertAndSend("reviews.update.queue", message);
        FileUtil.appendToFile("review_update.txt", message);
    }

    public void sendDeleteReviewMessage(String message) {
        jmsTemplate.convertAndSend("reviews.delete.queue", message);
        FileUtil.appendToFile("review_delete.txt", message);
    }

    public void sendReviewErrorMessage(String message) {
        jmsTemplate.convertAndSend("reviews.error.queue", message);
        FileUtil.appendToFile("review_error.txt", message);
    }
}
