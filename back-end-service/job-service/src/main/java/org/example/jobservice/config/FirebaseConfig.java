package org.example.jobservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    private ServiceAccountCredentials serviceAccountCredentials;

    @Bean
    public ServiceAccountCredentials serviceAccountCredentials() {
        return serviceAccountCredentials;
    }

    @PostConstruct
    public void initialize() {
        if (!FirebaseApp.getApps().isEmpty()) {
            log.info("Firebase Admin SDK already initialized.");
            return;
        }
        try {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream("firebase-service-account.json");

            if (serviceAccount == null) {
                log.error("firebase-service-account.json not found in src/main/resources -- messaging disabled. " +
                        "Download the key from Firebase Console -> Project Settings -> Service Accounts.");
                return;
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            String projectId = null;
            String keyId     = null;
            String account   = null;
            if (credentials instanceof ServiceAccountCredentials sa) {
                projectId = sa.getProjectId();
                keyId     = sa.getPrivateKeyId();
                account   = sa.getClientEmail();
                this.serviceAccountCredentials = sa;
            }

            log.info("Firebase Admin SDK loading -- project={} keyId={} account={}", projectId, keyId, account);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);

            log.info("Firebase Admin SDK initialized -- project={} keyId={}", projectId, keyId);

        } catch (IOException e) {
            log.error("Cannot read firebase-service-account.json: {}", e.getMessage());
        } catch (RuntimeException e) {
            log.error("Firebase Admin SDK initialization error: {}", e.getMessage());
        }
    }
}