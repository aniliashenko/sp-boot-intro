package mate.academy.intro.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemQuantityRequestDto {
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
