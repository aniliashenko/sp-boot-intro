package mate.academy.intro.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems = new HashSet<>();
}
