package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.CreateOrderRequestDto;
import mate.academy.intro.dto.OrderItemResponseDto;
import mate.academy.intro.dto.OrderResponseDto;
import mate.academy.intro.dto.UpdateOrderStatusRequestDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Place a new order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input or empty cart", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody @Valid CreateOrderRequestDto requestDto) {
        Long userId = getUserId(authentication);
        return orderService.placeOrder(userId, requestDto);
    }

    @Operation(summary = "Get user's orders with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found orders",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Page<OrderResponseDto> getOrders(Authentication authentication,
                                            @PageableDefault(size = 10,
                                                    sort = "orderDate") Pageable pageable) {
        Long userId = getUserId(authentication);
        return orderService.getUserOrders(userId, pageable);
    }

    @Operation(summary = "Get all items in an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order items",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponseDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Order not found or access denied", content = @Content)
    })
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<OrderItemResponseDto> getOrderItems(Authentication authentication,
                                                    @PathVariable Long orderId) {
        Long userId = getUserId(authentication);
        return orderService.getOrderItems(userId, orderId);
    }

    @Operation(summary = "Get specific item in an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderItemResponseDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Order item not found or access denied", content = @Content)
    })
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public OrderItemResponseDto getOrderItem(Authentication authentication,
                                             @PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        Long userId = getUserId(authentication);
        return orderService.getOrderItem(userId, orderId, itemId);
    }

    @Operation(summary = "Update order status (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OrderResponseDto updateStatus(@PathVariable Long orderId,
                            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
