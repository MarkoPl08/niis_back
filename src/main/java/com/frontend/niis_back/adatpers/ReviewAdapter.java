package com.frontend.niis_back.adatpers;

import com.frontend.niis_back.dto.ReviewDTO;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public class ReviewAdapter {

    private ReviewAdapter() {
        throw new UnsupportedOperationException("Utility class and cannot be instantiated");
    }

    public static ReviewDTO adapt(QueryDocumentSnapshot document) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(document.getId());
        dto.setTitle(document.getString("title"));
        dto.setText(document.getString("text"));
        Number rating = document.getDouble("rating");
        if (rating != null) {
            dto.setRating(rating.longValue());
        }
        dto.setHardwareId(document.getString("hardwareId"));
        return dto;
    }
}
