package com.prestek.people.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User data transfer object")
public class UserDto {
    
    @Schema(description = "User unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    
    @Schema(description = "User's first name", example = "John", required = true)
    private String firstName;
    
    @Schema(description = "User's last name", example = "Doe", required = true)
    private String lastName;
    
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;
    
    @Schema(description = "User's phone number", example = "+1234567890", required = true)
    private String phone;
    
    @Schema(description = "User's document number", example = "12345678", required = true)
    private String documentNumber;
    
    @Schema(description = "User's monthly income", example = "5000.00")
    private Double monthlyIncome;
    
    @Schema(description = "User's monthly expenses", example = "3000.00")
    private Double monthlyExpenses;
    
    @Schema(description = "User's credit score", example = "750")
    private Integer creditScore;
    
    @Schema(description = "User's employment status", example = "EMPLOYED")
    private String employmentStatus;
    
    @Schema(description = "User creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Schema(description = "User last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}