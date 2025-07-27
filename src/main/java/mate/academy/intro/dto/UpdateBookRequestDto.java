package mate.academy.intro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Request DTO for updating an existing book")
public class UpdateBookRequestDto {
    @NotBlank
    @Schema(description = "Updated title of the book",
            example = "The Catcher in the Rye (Revised Edition)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(description = "Updated author of the book",
            example = "J.D. Salinger",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @NotBlank
    @Schema(description = "Updated International Standard Book Number",
            example = "978-0-316-76948-0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @NotNull
    @Positive
    @Schema(description = "Updated price of the book in USD",
            example = "14.50",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Updated description of the book",
            example = "A story about teenage rebellion and alienation")
    private String description;

    @Schema(description = "Updated URL to the book cover image",
            example = "https://example.com/catcher-rye.jpg")
    private String coverImage;
}
