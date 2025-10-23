package mate.academy.intro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddBookToCartRequestDto {
    @NotNull(message = "Book ID must not be null")
    @Positive
    private Long bookId;

    @Positive
    private int quantity;
}
