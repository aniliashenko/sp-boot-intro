package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.AddBookToCartRequestDto;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.dto.UpdateCartItemQuantityRequestDto;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Management", description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get current user's shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shopping cart retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCartResponseDto.class))})
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartResponseDto getCart(@RequestParam Long userId) {
        return shoppingCartService.getUserCart(userId);
    }

    @Operation(summary = "Add book to shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added to cart",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShoppingCartResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Book or cart not found", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ShoppingCartResponseDto addBookToCart(
            @RequestParam Long userId,
            @RequestBody @Valid AddBookToCartRequestDto requestDto) {
        return shoppingCartService.addBookToCart(userId, requestDto);
    }

    @Operation(summary = "Update quantity of a cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart item updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartItemResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Cart item not found", content = @Content)
    })
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CartItemResponseDto updateCartItem(
            @RequestParam Long userId,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemQuantityRequestDto requestDto) {
        return shoppingCartService.updateCartItem(userId, cartItemId, requestDto);
    }

    @Operation(summary = "Remove a cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "404",
                    description = "Cart item not found", content = @Content)
    })
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public void removeCartItem(
            @RequestParam Long userId,
            @PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(userId, cartItemId);
    }
}
