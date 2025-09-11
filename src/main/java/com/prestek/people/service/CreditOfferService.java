package com.prestek.people.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prestek.people.dto.CreditOfferDto;
import com.prestek.people.model.CreditOffer;
import com.prestek.people.repository.CreditOfferRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreditOfferService {
    
    private final CreditOfferRepository creditOfferRepository;
    
    public List<CreditOfferDto> getAllCreditOffers() {
        log.info("Fetching all credit offers");
        return creditOfferRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CreditOfferDto> getActiveCreditOffers() {
        log.info("Fetching active credit offers");
        return creditOfferRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<CreditOfferDto> getCreditOfferById(Long id) {
        log.info("Fetching credit offer with id: {}", id);
        return creditOfferRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public List<CreditOfferDto> getCreditOffersByFinancialEntity(String financialEntity) {
        log.info("Fetching credit offers for financial entity: {}", financialEntity);
        return creditOfferRepository.findByFinancialEntity(financialEntity)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<CreditOfferDto> getCreditOffersByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.info("Fetching credit offers in amount range: {} - {}", minAmount, maxAmount);
        return creditOfferRepository.findActiveOffersByAmountRange(minAmount, maxAmount)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CreditOfferDto createCreditOffer(CreditOfferDto creditOfferDto) {
        log.info("Creating new credit offer for entity: {}", creditOfferDto.getFinancialEntity());
        
        CreditOffer creditOffer = convertToEntity(creditOfferDto);
        CreditOffer savedCreditOffer = creditOfferRepository.save(creditOffer);
        log.info("Credit offer created successfully with id: {}", savedCreditOffer.getId());
        
        return convertToDto(savedCreditOffer);
    }
    
    public Optional<CreditOfferDto> updateCreditOffer(Long id, CreditOfferDto creditOfferDto) {
        log.info("Updating credit offer with id: {}", id);
        
        return creditOfferRepository.findById(id)
                .map(existingOffer -> {
                    updateCreditOfferFields(existingOffer, creditOfferDto);
                    CreditOffer updatedOffer = creditOfferRepository.save(existingOffer);
                    log.info("Credit offer updated successfully with id: {}", updatedOffer.getId());
                    return convertToDto(updatedOffer);
                });
    }
    
    public boolean deleteCreditOffer(Long id) {
        log.info("Deleting credit offer with id: {}", id);
        
        if (creditOfferRepository.existsById(id)) {
            creditOfferRepository.deleteById(id);
            log.info("Credit offer deleted successfully with id: {}", id);
            return true;
        }
        
        log.warn("Credit offer not found for deletion with id: {}", id);
        return false;
    }
    
    public Optional<CreditOfferDto> deactivateCreditOffer(Long id) {
        log.info("Deactivating credit offer with id: {}", id);
        
        return creditOfferRepository.findById(id)
                .map(offer -> {
                    offer.setIsActive(false);
                    CreditOffer updatedOffer = creditOfferRepository.save(offer);
                    log.info("Credit offer deactivated successfully with id: {}", updatedOffer.getId());
                    return convertToDto(updatedOffer);
                });
    }
    
    private CreditOfferDto convertToDto(CreditOffer creditOffer) {
        return CreditOfferDto.builder()
                .id(creditOffer.getId())
                .amount(creditOffer.getAmount())
                .interestRate(creditOffer.getInterestRate())
                .termMonths(creditOffer.getTermMonths())
                .financialEntity(creditOffer.getFinancialEntity())
                .description(creditOffer.getDescription())
                .requirements(creditOffer.getRequirements())
                .isActive(creditOffer.getIsActive())
                .createdAt(creditOffer.getCreatedAt())
                .updatedAt(creditOffer.getUpdatedAt())
                .build();
    }
    
    private CreditOffer convertToEntity(CreditOfferDto creditOfferDto) {
        return CreditOffer.builder()
                .amount(creditOfferDto.getAmount())
                .interestRate(creditOfferDto.getInterestRate())
                .termMonths(creditOfferDto.getTermMonths())
                .financialEntity(creditOfferDto.getFinancialEntity())
                .description(creditOfferDto.getDescription())
                .requirements(creditOfferDto.getRequirements())
                .isActive(creditOfferDto.getIsActive())
                .build();
    }
    
    private void updateCreditOfferFields(CreditOffer creditOffer, CreditOfferDto creditOfferDto) {
        if (creditOfferDto.getAmount() != null) {
            creditOffer.setAmount(creditOfferDto.getAmount());
        }
        if (creditOfferDto.getInterestRate() != null) {
            creditOffer.setInterestRate(creditOfferDto.getInterestRate());
        }
        if (creditOfferDto.getTermMonths() != null) {
            creditOffer.setTermMonths(creditOfferDto.getTermMonths());
        }
        if (creditOfferDto.getFinancialEntity() != null) {
            creditOffer.setFinancialEntity(creditOfferDto.getFinancialEntity());
        }
        if (creditOfferDto.getDescription() != null) {
            creditOffer.setDescription(creditOfferDto.getDescription());
        }
        if (creditOfferDto.getRequirements() != null) {
            creditOffer.setRequirements(creditOfferDto.getRequirements());
        }
        if (creditOfferDto.getIsActive() != null) {
            creditOffer.setIsActive(creditOfferDto.getIsActive());
        }
    }
}