package com.frontend.niis_back.controllers;

import com.frontend.niis_back.dto.RestaurantDTO;
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

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurant() {
        try {
            List<RestaurantDTO> restaurantDTOList = restaurantService.findAllRestaurant();
            return new ResponseEntity<>(restaurantDTOList, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        try {
            String id = restaurantService.saveRestaurant(restaurantDTO);

            RestaurantDTO responseDTO = new RestaurantDTO();
            responseDTO.setId(id);
            responseDTO.setName(restaurantDTO.getName());
            responseDTO.setZipCode(restaurantDTO.getZipCode());
            responseDTO.setLocation(restaurantDTO.getLocation());

            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(@PathVariable String id, @RequestBody RestaurantDTO restaurantDTO) {
        try {
            restaurantDTO.setId(id);

            restaurantService.updateRestaurant(restaurantDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String restaurantId) {
        try {
            restaurantService.deleteRestaurant(restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

