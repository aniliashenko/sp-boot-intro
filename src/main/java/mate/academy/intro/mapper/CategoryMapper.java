package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CategoryRequestDto;
import mate.academy.intro.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);

    void updateCategoryFromDto(CategoryRequestDto categoryDto, @MappingTarget Category category);
}
