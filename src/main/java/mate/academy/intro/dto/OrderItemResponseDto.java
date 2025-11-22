package mate.academy.intro.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
    private BigDecimal price;
}
