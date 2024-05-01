package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.service.MessageService;
import com.frontend.niis_back.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final MessageService messageService;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @Autowired
    public RestaurantController(RestaurantService restaurantService, MessageService messageService) {
        this.restaurantService = restaurantService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurant() {
        try {
            List<RestaurantDTO> restaurantDTOList = restaurantService.findAllRestaurant();
            messageService.sendGetRestaurantMessage("Successfully retrieved all the restaurant data");
            return ResponseEntity.ok(restaurantDTOList);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Error in retrieving all the restaurant data");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        if (restaurantDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            String id = restaurantService.saveRestaurant(restaurantDTO);
            messageService.sendCreateRestaurantMessage("Restaurant created with ID: " + id);
            restaurantDTO.setId(id);
            return new ResponseEntity<>(restaurantDTO, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Error creating restaurant: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable String id, @RequestBody RestaurantDTO restaurantDTO) {
        if (restaurantDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            restaurantDTO.setId(id);
            restaurantService.updateRestaurant(restaurantDTO);
            messageService.sendUpdateRestaurantMessage("Restaurant updated with ID: " + id);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Thread was interrupted while updating restaurant with ID: " + id);
            logger.error("Thread interruption occurred while updating restaurant.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            messageService.sendRestaurantErrorMessage("Error updating restaurant with ID: " + id);
            logger.error("Error updating restaurant with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            messageService.sendDeleteRestaurantMessage("Restaurant deleted with ID: " + restaurantId);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Thread was interrupted while deleting restaurant with ID: " + restaurantId);
            logger.error("Thread interruption occurred while deleting restaurant.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            messageService.sendRestaurantErrorMessage("Error deleting restaurant with ID: " + restaurantId);
            logger.error("Error deleting restaurant with ID: {}", restaurantId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
