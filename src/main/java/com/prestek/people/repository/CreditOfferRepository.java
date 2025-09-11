package com.prestek.people.repository;

import com.prestek.people.model.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CreditOfferRepository extends JpaRepository<CreditOffer, Long> {
    
    List<CreditOffer> findByIsActiveTrue();
    
    List<CreditOffer> findByFinancialEntity(String financialEntity);
    
    @Query("SELECT co FROM CreditOffer co WHERE co.isActive = true AND co.amount >= :minAmount AND co.amount <= :maxAmount")
    List<CreditOffer> findActiveOffersByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT co FROM CreditOffer co WHERE co.isActive = true AND co.interestRate <= :maxRate")
    List<CreditOffer> findActiveOffersByMaxInterestRate(@Param("maxRate") BigDecimal maxRate);
}