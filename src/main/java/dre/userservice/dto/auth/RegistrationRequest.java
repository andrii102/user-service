package dre.userservice.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password,

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "^\\+?[0-9\\-\\s()]{7,15}$", message = "Phone number format is invalid")
        String phoneNumber,

        @NotBlank(message = "Access card ID cannot be blank")
        @Size(min = 4, max = 50, message = "Access card ID must be between 4 and 50 characters")
        String accessCardId,

        String address
) {
}
