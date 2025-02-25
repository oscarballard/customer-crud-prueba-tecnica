package org.acme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerDto(
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be in a valid format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,

    @NotBlank(message = "Phone number cannot be empty")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    String phone,

    @NotBlank(message = "Country code cannot be empty")
    @Pattern(regexp = "^[A-Z]{2,3}$", message = "Country code must be an ISO 3166 code with 2 or 3 uppercase letters")
    @Size(max = 3, message = "Country code must be an ISO 3166 code with 2 or 3 uppercase letters")
    String countryCode
) {}