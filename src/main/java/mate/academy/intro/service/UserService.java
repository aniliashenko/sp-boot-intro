package mate.academy.intro.service;

import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserRegistrationResponseDto;
import mate.academy.intro.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
