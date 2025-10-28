package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.CreateOrderRequestDto;
import mate.academy.intro.dto.OrderItemResponseDto;
import mate.academy.intro.dto.OrderResponseDto;
import mate.academy.intro.dto.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable);

    List<OrderItemResponseDto> getOrderItems(Long userId, Long orderId);

    OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);
}
