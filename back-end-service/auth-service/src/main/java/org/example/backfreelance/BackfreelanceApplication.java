package org.example.backfreelance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BackfreelanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackfreelanceApplication.class, args);
    }
}
