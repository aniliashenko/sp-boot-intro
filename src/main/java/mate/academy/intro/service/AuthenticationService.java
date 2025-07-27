package mate.academy.intro.service;

import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;

public interface AuthenticationService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
