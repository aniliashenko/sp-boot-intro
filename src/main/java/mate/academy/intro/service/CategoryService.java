package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryDto);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
