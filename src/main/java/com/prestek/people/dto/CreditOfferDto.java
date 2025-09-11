package com.prestek.people.dto;

import java.math.BigDecimal;
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
@Schema(description = "Credit offer data transfer object")
public class CreditOfferDto {
    
    @Schema(description = "Credit offer unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Credit amount", example = "50000.00", required = true)
    private BigDecimal amount;
    
    @Schema(description = "Interest rate percentage", example = "12.5", required = true)
    private BigDecimal interestRate;
    
    @Schema(description = "Term in months", example = "24", required = true)
    private Integer termMonths;
    
    @Schema(description = "Financial entity offering the credit", example = "Banco Nacional", required = true)
    private String financialEntity;
    
    @Schema(description = "Credit offer description", example = "Personal loan with competitive rates")
    private String description;
    
    @Schema(description = "Requirements for the credit", example = "Minimum income: $2000, Good credit score")
    private String requirements;
    
    @Schema(description = "Whether the offer is currently active", example = "true")
    private Boolean isActive;
    
    @Schema(description = "Credit offer creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @Schema(description = "Credit offer last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}