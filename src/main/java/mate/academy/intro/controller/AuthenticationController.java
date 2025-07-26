package mate.academy.intro.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @PostMapping("/register")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return null;
    }
}
