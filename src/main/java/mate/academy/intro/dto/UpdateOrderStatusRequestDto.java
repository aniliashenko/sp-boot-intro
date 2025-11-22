package mate.academy.intro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.intro.model.OrderStatus;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private OrderStatus status;
}
