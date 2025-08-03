package mate.academy.intro.service;

import mate.academy.intro.dto.UserLoginRequestDto;
import mate.academy.intro.dto.UserLoginResponseDto;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserRegistrationResponseDto;
import mate.academy.intro.exception.RegistrationException;

public interface AuthenticationService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;

    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
