package com.prestek.people.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prestek.people.dto.CreditOfferDto;
import com.prestek.people.service.CreditOfferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/credit-offers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Credit Offers", description = "Credit offer management operations")
public class CreditOfferController {
    
    private final CreditOfferService creditOfferService;
    
    @GetMapping
    @Operation(summary = "Get all credit offers", description = "Retrieve a list of all credit offers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved credit offers",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = CreditOfferDto.class))))
    })
    public ResponseEntity<List<CreditOfferDto>> getAllCreditOffers() {
        log.info("GET /api/credit-offers - Fetching all credit offers");
        List<CreditOfferDto> offers = creditOfferService.getAllCreditOffers();
        return ResponseEntity.ok(offers);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get active credit offers", description = "Retrieve all currently active credit offers")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active credit offers",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = CreditOfferDto.class))))
    })
    public ResponseEntity<List<CreditOfferDto>> getActiveCreditOffers() {
        log.info("GET /api/credit-offers/active - Fetching active credit offers");
        List<CreditOfferDto> offers = creditOfferService.getActiveCreditOffers();
        return ResponseEntity.ok(offers);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get credit offer by ID", description = "Retrieve a specific credit offer by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit offer found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CreditOfferDto.class))),
        @ApiResponse(responseCode = "404", description = "Credit offer not found")
    })
    public ResponseEntity<CreditOfferDto> getCreditOfferById(
            @Parameter(description = "Credit offer ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("GET /api/credit-offers/{} - Fetching credit offer by id", id);
        return creditOfferService.getCreditOfferById(id)
                .map(offer -> ResponseEntity.ok(offer))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/entity/{financialEntity}")
    @Operation(summary = "Get credit offers by financial entity", description = "Retrieve credit offers from a specific financial institution")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved credit offers",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = CreditOfferDto.class))))
    })
    public ResponseEntity<List<CreditOfferDto>> getCreditOffersByFinancialEntity(
            @Parameter(description = "Financial entity name", required = true, example = "Banco Nacional")
            @PathVariable String financialEntity) {
        log.info("GET /api/credit-offers/entity/{} - Fetching credit offers by financial entity", financialEntity);
        List<CreditOfferDto> offers = creditOfferService.getCreditOffersByFinancialEntity(financialEntity);
        return ResponseEntity.ok(offers);
    }
    
    @GetMapping("/amount-range")
    @Operation(summary = "Get credit offers by amount range", description = "Retrieve credit offers within a specific amount range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved credit offers in range",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = CreditOfferDto.class))))
    })
    public ResponseEntity<List<CreditOfferDto>> getCreditOffersByAmountRange(
            @Parameter(description = "Minimum amount", required = true, example = "10000")
            @RequestParam BigDecimal minAmount,
            @Parameter(description = "Maximum amount", required = true, example = "100000")
            @RequestParam BigDecimal maxAmount) {
        log.info("GET /api/credit-offers/amount-range?minAmount={}&maxAmount={} - Fetching credit offers by amount range", minAmount, maxAmount);
        List<CreditOfferDto> offers = creditOfferService.getCreditOffersByAmountRange(minAmount, maxAmount);
        return ResponseEntity.ok(offers);
    }
    
    @PostMapping
    @Operation(summary = "Create new credit offer", description = "Create a new credit offer in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credit offer created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CreditOfferDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CreditOfferDto> createCreditOffer(
            @Parameter(description = "Credit offer data", required = true)
            @RequestBody CreditOfferDto creditOfferDto) {
        log.info("POST /api/credit-offers - Creating new credit offer");
        CreditOfferDto createdOffer = creditOfferService.createCreditOffer(creditOfferDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOffer);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update credit offer", description = "Update an existing credit offer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit offer updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CreditOfferDto.class))),
        @ApiResponse(responseCode = "404", description = "Credit offer not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<CreditOfferDto> updateCreditOffer(
            @Parameter(description = "Credit offer ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated credit offer data", required = true)
            @RequestBody CreditOfferDto creditOfferDto) {
        log.info("PUT /api/credit-offers/{} - Updating credit offer", id);
        return creditOfferService.updateCreditOffer(id, creditOfferDto)
                .map(offer -> ResponseEntity.ok(offer))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate credit offer", description = "Mark a credit offer as inactive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credit offer deactivated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = CreditOfferDto.class))),
        @ApiResponse(responseCode = "404", description = "Credit offer not found")
    })
    public ResponseEntity<CreditOfferDto> deactivateCreditOffer(
            @Parameter(description = "Credit offer ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("PATCH /api/credit-offers/{}/deactivate - Deactivating credit offer", id);
        return creditOfferService.deactivateCreditOffer(id)
                .map(offer -> ResponseEntity.ok(offer))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete credit offer", description = "Remove a credit offer from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Credit offer deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Credit offer not found")
    })
    public ResponseEntity<Void> deleteCreditOffer(
            @Parameter(description = "Credit offer ID", required = true, example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/credit-offers/{} - Deleting credit offer", id);
        boolean deleted = creditOfferService.deleteCreditOffer(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}