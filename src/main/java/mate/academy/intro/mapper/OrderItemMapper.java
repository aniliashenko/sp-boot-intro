package mate.academy.intro.mapper;

import java.util.List;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.OrderItemResponseDto;
import mate.academy.intro.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemResponseDto toDto(OrderItem item);

    List<OrderItemResponseDto> toDtoList(List<OrderItem> items);
}
