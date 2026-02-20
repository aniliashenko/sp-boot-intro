package mate.academy.intro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Category DTO containing complete category information")
public class CategoryDto {
    @Schema(description = "Name of the category",
            example = "Detective",
            required = true)
    private String name;

    @Schema(description = "Detailed description of the category",
            example = "A genre of mystery and very smart people")
    private String description;
}
