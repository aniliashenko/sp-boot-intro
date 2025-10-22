package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    CartItem fromBookId(Long bookId);

    @AfterMapping
    default void setBookTitle(@MappingTarget CartItemResponseDto dto, CartItem cartItem) {
        if (cartItem.getBook() != null) {
            dto.setBookTitle(cartItem.getBook().getTitle());
        }
    }
}
