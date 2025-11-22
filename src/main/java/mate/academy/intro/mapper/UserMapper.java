package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserRegistrationResponseDto;
import mate.academy.intro.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
