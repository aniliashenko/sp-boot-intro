package mate.academy.intro.service.impl;

import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.RoleRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private static final Role.RoleName ROLE_NAME_USER = Role.RoleName.USER;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            LOGGER.warn("Registration failed: Email {} already exists", requestDto.getEmail());
            throw new RegistrationException("User with email "
                    + requestDto.getEmail() + " already exists");
        }

        User user = userMapper.toModel(requestDto);

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByRoleName(ROLE_NAME_USER)
                .orElseThrow(() -> {
                    LOGGER.error("ROLE_USER with id {} not found", ROLE_NAME_USER);
                    return new RegistrationException("Default role not found");
                });
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
        LOGGER.info("User registered successfully: {}", user.getEmail());

        return userMapper.toDto(user);
    }
}
