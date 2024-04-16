package com.frontend.niis_back.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore firestore() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource("/final-niis-firebase-adminsdk-vbgni-d51da8882f.json").getInputStream());
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        return FirestoreClient.getFirestore();
    }
}

