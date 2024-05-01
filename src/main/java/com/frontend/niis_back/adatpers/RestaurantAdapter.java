package com.frontend.niis_back.adatpers;

import com.frontend.niis_back.dto.RestaurantDTO;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class RestaurantAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantAdapter.class);

    private RestaurantAdapter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static RestaurantDTO adapt(QueryDocumentSnapshot document) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(document.getId());

        try {
            dto.setLocation(document.getString("location"));
        } catch (ClassCastException e) {
            logger.error("Error casting 'location' to String in document {}", document.getId(), e);
        }

        try {
            dto.setName(document.getString("name"));
        } catch (ClassCastException e) {
            logger.error("Error casting 'name' to String in document {}", document.getId(), e);
        }

        try {
            Number zipCode = document.getDouble("zipCode");
            if (zipCode != null) {
                dto.setZipCode(new BigDecimal(zipCode.toString()));
            }
        } catch (ClassCastException e) {
            logger.error("Error casting 'zipCode' to Number in document {}", document.getId(), e);
        }

        return dto;
    }
}
