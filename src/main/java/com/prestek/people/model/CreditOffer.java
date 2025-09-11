package com.prestek.people.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "credit_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditOffer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private BigDecimal interestRate;
    
    @Column(nullable = false)
    private Integer termMonths;
    
    @Column(nullable = false)
    private String financialEntity;
    
    private String description;
    
    private String requirements;
    
    @Column(nullable = false)
    private Boolean isActive;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Relationship with applications
    @OneToMany(mappedBy = "creditOffer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}