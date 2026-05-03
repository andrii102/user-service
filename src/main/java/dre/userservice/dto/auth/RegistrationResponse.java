package dre.userservice.dto.auth;

import dre.userservice.model.UserRole;
import lombok.Builder;

@Builder
public record RegistrationResponse (
        Long id,
        String email,
        String username,
        String firstName,
        String lastName,
        String phoneNumber,
        String accessCardId,
        UserRole role
){

}
