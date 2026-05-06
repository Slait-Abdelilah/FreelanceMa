package org.example.userservice.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class UpdateAccountRequest {
    private String phone;
    private String language;
    private String currency;
}
