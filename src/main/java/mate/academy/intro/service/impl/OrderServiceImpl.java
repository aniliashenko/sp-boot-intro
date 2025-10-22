package mate.academy.intro.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.CreateOrderRequestDto;
import mate.academy.intro.dto.OrderItemResponseDto;
import mate.academy.intro.dto.OrderResponseDto;
import mate.academy.intro.dto.UpdateOrderStatusRequestDto;
import mate.academy.intro.mapper.OrderItemMapper;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.OrderItem;
import mate.academy.intro.model.OrderStatus;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.OrderItemRepository;
import mate.academy.intro.repository.OrderRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository cartRepository;

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(Long userId, CreateOrderRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            total = total.add(cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setTotal(total);
        orderRepository.save(order);

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return orderMapper.toDto(order);
    }

    @Override
    public Page<OrderResponseDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found or access denied"));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found or access denied"));
        OrderItem item = orderItemRepository.findByIdAndOrderId(itemId, order.getId())
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        return orderItemMapper.toDto(item);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }
}
