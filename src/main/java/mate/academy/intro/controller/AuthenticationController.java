package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserLoginRequestDto;
import mate.academy.intro.dto.UserLoginResponseDto;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserRegistrationResponseDto;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.service.AuthenticationService;
import mate.academy.intro.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user account and returns the created user data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRegistrationResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\":"
                                            + "\"Validation failed: email must be valid\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User with given email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"User with this email already exists\"}"
                            )
                    )
            )
    })
    @PostMapping("/registration")
    public UserRegistrationResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Logs in an existing user and returns an authentication "
                    + "token with user details."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully authenticated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserLoginResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Validation failed: "
                                            + "email and password must not be blank\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"error\": \"Invalid email or password\"}"
                            )
                    )
            )
    })
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
