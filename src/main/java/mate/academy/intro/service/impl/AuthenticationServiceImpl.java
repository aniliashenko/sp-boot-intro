package mate.academy.intro.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.AuthenticationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        Optional<User> existingUser = userRepository.existByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RegistrationException("User with this email already exists");
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(requestDto.getPassword());

        return userMapper.toDto(userRepository.save(user));
    }
}
