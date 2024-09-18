package com.pomegranate.pomemusic.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDto (
    @NotBlank(message = "Name is required")
    String name, 

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    String email, 

    @NotBlank(message = "Password is required")
    @Size(min = 7, message = "Password must be at least 7 characters")
    String password, 

    @NotBlank(message = "Username is required")
    String username,

    @NotNull(message = "Year of birth is required")
    @Min(1900)
    @Max(2024)
    Integer yearOfBirth,

    @NotBlank(message = "Avatar is required")
    @URL(message = "Avatar must be a valid URL")
    String avatar
    
    ) {}
