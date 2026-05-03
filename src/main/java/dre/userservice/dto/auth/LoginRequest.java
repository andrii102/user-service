package dre.userservice.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Identifier (username or email) cannot be blank")
        @Size(min = 3, max = 100, message = "Identifier must be between 3 and 100 characters")
        String identifier,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password
) {
}
