package mate.academy.intro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "must not be blank")
    private String name;

    private String description;
}
