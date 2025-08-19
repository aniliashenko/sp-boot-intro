package mate.academy.intro.service;

import mate.academy.intro.dto.UserLoginRequestDto;
import mate.academy.intro.dto.UserLoginResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto requestDto);
}
