package mate.academy.intro.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.OrderItemResponseDto;
import mate.academy.intro.dto.OrderResponseDto;
import mate.academy.intro.model.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @AfterMapping
    default void setOrderItems(@MappingTarget OrderResponseDto dto, Order order) {
        if (order.getOrderItems() != null) {
            Set<OrderItemResponseDto> items = order.getOrderItems().stream()
                    .map(item -> {
                        OrderItemResponseDto itemDto = new OrderItemResponseDto();
                        itemDto.setId(item.getId());
                        itemDto.setBookId(item.getBook().getId());
                        itemDto.setQuantity(item.getQuantity());
                        itemDto.setPrice(item.getPrice());
                        return itemDto;
                    })
                    .collect(Collectors.toSet());
            dto.setOrderItems(items);
        }
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setShippingAddress(order.getShippingAddress());
    }
}
