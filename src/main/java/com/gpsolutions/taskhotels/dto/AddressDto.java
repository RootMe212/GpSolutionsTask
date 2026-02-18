package com.gpsolutions.taskhotels.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
    @NotNull(message = "House number is required")
    Integer houseNumber,

    @NotBlank(message = "Street is required")
    String street,

    @NotBlank(message = "City is required")
    String city,

    @NotBlank(message = "Country is required")
    String country,

    @NotBlank(message = "Postcode is required")
    String postCode
){}
