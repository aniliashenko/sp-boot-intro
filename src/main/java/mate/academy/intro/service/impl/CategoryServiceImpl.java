package mate.academy.intro.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Cannot update. Category not found with id: " + id));

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("Cannot delete. Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
