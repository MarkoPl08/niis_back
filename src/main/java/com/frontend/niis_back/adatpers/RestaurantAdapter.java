package com.frontend.niis_back.adatpers;

import com.frontend.niis_back.dto.RestaurantDTO;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.math.BigDecimal;

public class RestaurantAdapter {
    public static RestaurantDTO adapt(QueryDocumentSnapshot document) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(document.getId());
        dto.setLocation(document.getString("location"));
        dto.setName(document.getString("name"));
        Number zipCode = document.getDouble("zipCode");
        if (zipCode != null) {
            dto.setZipCode(new BigDecimal(zipCode.toString()));
        }
        return dto;
    }
}
