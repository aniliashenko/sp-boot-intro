package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import mate.academy.intro.dto.AddBookToCartRequestDto;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.dto.UpdateCartItemQuantityRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
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
import mate.academy.intro.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Get existing shopping cart by user id")
    void getUserCart_ValidUserId_ReturnsCart() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(10L);
        cart.setUser(user);

        ShoppingCartResponseDto cartDto = new ShoppingCartResponseDto();
        cartDto.setId(10L);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(cartDto);

        ShoppingCartResponseDto result = shoppingCartService.getUserCart(userId);

        assertEquals(cartDto, result);
        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartMapper).toDto(cart);
    }

    @Test
    @DisplayName("Get shopping cart for non-existing user throws exception")
    void getUserCart_InvalidUserId_ThrowsException() {
        Long userId = 999L;
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getUserCart(userId));
    }

    @Test
    @DisplayName("Add new book to empty cart")
    void addBookToCart_NewBook_CreatesCartItem() {
        Long userId = 1L;
        Long bookId = 100L;
        User user = new User();
        user.setId(userId);

        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(bookId);
        requestDto.setQuantity(2);

        Book book = new Book();
        book.setId(bookId);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        ShoppingCart savedCart = new ShoppingCart();
        savedCart.setUser(user);
        savedCart.setCartItems(new HashSet<>());

        ShoppingCartResponseDto cartDto = new ShoppingCartResponseDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(savedCart);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toDto(savedCart)).thenReturn(cartDto);

        ShoppingCartResponseDto result = shoppingCartService.addBookToCart(userId, requestDto);

        assertEquals(cartDto, result);
        verify(userRepository).findById(userId);
        verify(shoppingCartRepository, times(2)).save(any(ShoppingCart.class));
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Update cart item quantity")
    void updateCartItem_Valid_ReturnsUpdatedDto() {
        final Long userId = 1L;
        Long cartItemId = 10L;

        UpdateCartItemQuantityRequestDto requestDto = new UpdateCartItemQuantityRequestDto();
        requestDto.setQuantity(5);

        CartItem item = new CartItem();
        item.setId(cartItemId);
        item.setQuantity(2);

        CartItemResponseDto itemDto = new CartItemResponseDto();
        itemDto.setQuantity(5);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(20L);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId()))
                .thenReturn(Optional.of(item));
        when(cartItemMapper.toDto(item)).thenReturn(itemDto);

        CartItemResponseDto result = shoppingCartService
                .updateCartItem(userId, cartItemId, requestDto);

        assertEquals(itemDto, result);
        verify(cartItemRepository).save(item);
    }

    @Test
    @DisplayName("Remove cart item")
    void removeCartItem_Valid_RemovesItem() {
        Long userId = 1L;
        Long cartItemId = 10L;

        CartItem item = new CartItem();
        item.setId(cartItemId);

        ShoppingCart cart = new ShoppingCart();
        cart.setId(20L);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId()))
                .thenReturn(Optional.of(item));

        shoppingCartService.removeCartItem(userId, cartItemId);

        verify(cartItemRepository).delete(item);
    }

    @Test
    @DisplayName("Remove non-existing cart item throws exception")
    void removeCartItem_Invalid_ThrowsException() {
        Long userId = 1L;
        Long cartItemId = 999L;

        ShoppingCart cart = new ShoppingCart();
        cart.setId(20L);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.removeCartItem(userId, cartItemId));
    }
}
