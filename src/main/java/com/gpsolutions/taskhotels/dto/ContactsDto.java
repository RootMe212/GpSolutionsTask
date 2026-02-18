package com.gpsolutions.taskhotels.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactsDto (
    @NotBlank(message = "Phone is required")
    String phone,

    @Email(message = "Invalid Email format")
    @NotBlank(message = "Email is required")
    String email
){}
