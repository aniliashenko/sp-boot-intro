package mate.academy.intro.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart cart);

    @AfterMapping
    default void setCartItems(@MappingTarget ShoppingCartResponseDto dto, ShoppingCart cart) {
        if (cart.getCartItems() != null) {
            Set<CartItemResponseDto> items = cart.getCartItems().stream()
                    .map(item -> {
                        CartItemResponseDto cartItemDto = new CartItemResponseDto();
                        cartItemDto.setId(item.getId());
                        cartItemDto.setBookId(item.getBook().getId());
                        cartItemDto.setBookTitle(item.getBook().getTitle());
                        cartItemDto.setQuantity(item.getQuantity());
                        return cartItemDto;
                    })
                    .collect(Collectors.toSet());
            dto.setCartItems(items);
        }
        if (cart.getUser() != null) {
            dto.setUserId(cart.getUser().getId());
        }
        dto.setId(cart.getId());
    }
}
