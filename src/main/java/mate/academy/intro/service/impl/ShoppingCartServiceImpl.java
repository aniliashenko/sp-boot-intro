package mate.academy.intro.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.AddBookToCartRequestDto;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.dto.UpdateCartItemQuantityRequestDto;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getUserCart(Long userId) {
        ShoppingCart cart = (ShoppingCart) shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException(
                        "Shopping cart not found for user " + userId));

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(Long userId,
                                                 AddBookToCartRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + requestDto.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setShoppingCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(requestDto.getQuantity());
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public CartItemResponseDto updateCartItem(Long userId, Long cartItemId,
                                              UpdateCartItemQuantityRequestDto requestDto) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getShoppingCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("You can't update this cart item");
        }

        item.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(item);

        return cartItemMapper.toDto(item);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getShoppingCart().getUser().getId().equals(userId)) {
            throw new RuntimeException("You can't delete this cart item");
        }

        cartItemRepository.delete(item);
    }
}
