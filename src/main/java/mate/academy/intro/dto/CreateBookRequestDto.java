package mate.academy.intro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import mate.academy.intro.model.Category;

@Data
@Schema(description = "Request DTO for creating a new book")
public class CreateBookRequestDto {
    @NotBlank
    @Schema(description = "Title of the book",
            example = "To Kill a Mockingbird",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank
    @Schema(description = "Author of the book",
            example = "Harper Lee",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String author;

    @NotBlank
    @Schema(description = "International Standard Book Number",
            example = "978-0-06-112008-4",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @NotNull
    @Positive
    @Schema(description = "Price of the book in USD",
            example = "12.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

    @Schema(description = "Detailed description of the book",
            example = "A novel about racial injustice and moral growth in the American South")
    private String description;

    @Schema(description = "URL to the book cover image",
            example = "https://example.com/mockingbird.jpg")
    private String coverImage;

    private List<Long> categoryIds;
}
