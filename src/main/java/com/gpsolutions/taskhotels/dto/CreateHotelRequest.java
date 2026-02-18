package com.gpsolutions.taskhotels.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHotelRequest (
    @NotBlank(message = "Name is required")
    String name,

    String description,

    @NotBlank(message = "Brand is required")
    String brand,

    @NotNull(message = "Address is required")
    @Valid
    AddressDto address,

    @NotNull(message = "Contacts is required")
    @Valid
    ContactsDto contacts,

    @Valid
    ArrivalTimeDto arrivalTime
){

}
