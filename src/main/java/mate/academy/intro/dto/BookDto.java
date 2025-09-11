package mate.academy.intro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import mate.academy.intro.model.Category;

@Data
@Schema(description = "Book data transfer object containing complete book information")
public class BookDto {
    @Schema(description = "Title of the book", example = "The Great Gatsby", required = true)
    private String title;

    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald", required = true)
    private String author;

    @Schema(description = "International Standard Book Number",
            example = "978-3-16-148410-0",
            required = true)
    private String isbn;

    @Schema(description = "Price of the book in USD",
            example = "19.99",
            required = true)
    private BigDecimal price;

    @Schema(description = "Detailed description of the book",
            example = "A story of wealth, love, and the American Dream in the 1920s")
    private String description;

    @Schema(description = "URL to the book cover image",
            example = "https://example.com/great-gatsby.jpg")
    private String coverImage;

    @Schema(description = "Categories of the book",
            example = "Romance, Fantasy")
    private Set<Category> categories = new HashSet<>();
}
