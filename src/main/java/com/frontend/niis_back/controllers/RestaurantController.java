package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.RestaurantDTO;
import com.frontend.niis_back.service.MessageService;
import com.frontend.niis_back.service.RestaurantService;
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
            return new ResponseEntity<>(restaurantDTOList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Error in retrieving all the restaurant data");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        try {
            String id = restaurantService.saveRestaurant(restaurantDTO);

            messageService.sendCreateRestaurantMessage("Restaurant created with ID: " + id);

            RestaurantDTO responseDTO = new RestaurantDTO();
            responseDTO.setId(id);
            responseDTO.setName(restaurantDTO.getName());
            responseDTO.setZipCode(restaurantDTO.getZipCode());
            responseDTO.setLocation(restaurantDTO.getLocation());

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            messageService.sendRestaurantErrorMessage("Error creating restaurant: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable String id, @RequestBody RestaurantDTO restaurantDTO) {
        try {
            restaurantDTO.setId(id);

            restaurantService.updateRestaurant(restaurantDTO);

            messageService.sendUpdateRestaurantMessage("Restaurant updated with ID: " + id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            messageService.sendRestaurantErrorMessage("Error updating restaurant with ID: " + id);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            messageService.sendDeleteRestaurantMessage("Restaurant deleted with ID: " + restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            messageService.sendRestaurantErrorMessage("Error deleting restaurant with ID: " + restaurantId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

