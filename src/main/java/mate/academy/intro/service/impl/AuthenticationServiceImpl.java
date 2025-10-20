package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserLoginRequestDto;
import mate.academy.intro.dto.UserLoginResponseDto;
import mate.academy.intro.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager manager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = manager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.getEmail(), requestDto.getPassword()));
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
