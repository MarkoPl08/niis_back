package com.frontend.niis_back.adatpers;

import com.frontend.niis_back.dto.RestaurantDTO;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.math.BigDecimal;

public class RestaurantAdapter {
    public static RestaurantDTO adapt(QueryDocumentSnapshot document) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(document.getId());

        try {
            dto.setLocation(document.getString("location"));
        } catch (ClassCastException e) {
            System.err.println("Error casting 'location' to String in document " + document.getId());
        }

        try {
            dto.setName(document.getString("name"));
        } catch (ClassCastException e) {
            System.err.println("Error casting 'name' to String in document " + document.getId());
        }

        try {
            Number zipCode = document.getDouble("zipCode");
            if (zipCode != null) {
                dto.setZipCode(new BigDecimal(zipCode.toString()));
            }
        } catch (ClassCastException e) {
            System.err.println("Error casting 'zipCode' to Number in document " + document.getId());
        }

        return dto;
    }
}
