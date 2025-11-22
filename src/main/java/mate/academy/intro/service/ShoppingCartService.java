package mate.academy.intro.service;

import mate.academy.intro.dto.AddBookToCartRequestDto;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.dto.UpdateCartItemQuantityRequestDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getUserCart(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, AddBookToCartRequestDto requestDto);

    CartItemResponseDto updateCartItem(Long userId, Long cartItemId,
                                       UpdateCartItemQuantityRequestDto requestDto);

    void removeCartItem(Long userId, Long cartItemId);
}
