package com.prestek.people.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create application using User Id and Credit Offer Id")
public class CreateApplicationDto {
    @NotBlank String userId;
    @NotNull
    Long creditOfferId;


}
