package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CategoryRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories")
    void findAll_ReturnsListOfCategoryDto() {
        // Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Fiction");
        category1.setDescription("Fiction books");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Science");
        category2.setDescription("Science books");

        CategoryDto dto1 = new CategoryDto();
        dto1.setId(1L);
        dto1.setName("Fiction");
        dto1.setDescription("Fiction books");

        CategoryDto dto2 = new CategoryDto();
        dto2.setId(2L);
        dto2.setName("Science");
        dto2.setDescription("Science books");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryMapper.toDto(category1)).thenReturn(dto1);
        when(categoryMapper.toDto(category2)).thenReturn(dto2);

        // When
        List<CategoryDto> result = categoryService.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(categoryRepository).findAll();
        verify(categoryMapper, times(2)).toDto(any(Category.class));
    }

    @Test
    @DisplayName("Get category by existing id")
    void getById_ValidId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fiction");
        category.setDescription("Fiction books");

        CategoryDto expected = new CategoryDto();
        expected.setId(categoryId);
        expected.setName("Fiction");
        expected.setDescription("Fiction books");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.getById(categoryId);

        // Then
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by non-existing id")
    void getById_InvalidId_ThrowsEntityNotFoundException() {
        // Given
        Long invalidId = 999L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(invalidId));
        verify(categoryRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Save new category")
    void save_ValidRequestDto_ReturnsCategoryDto() {
        // Given
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");
        requestDto.setDescription("Fiction books");

        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fiction books");
        category.setId(1L);

        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Fiction");
        expected.setDescription("Fiction books");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.save(requestDto);

        // Then
        assertEquals(expected, actual);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update existing category")
    void update_ValidId_ReturnsUpdatedCategoryDto() {
        // Given
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Fiction");
        requestDto.setDescription("Updated description");

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Fiction");
        existingCategory.setDescription("Old description");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName("Updated Fiction");
        updatedCategory.setDescription("Updated description");

        CategoryDto expected = new CategoryDto();
        expected.setId(categoryId);
        expected.setName("Updated Fiction");
        expected.setDescription("Updated description");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.update(categoryId, requestDto);

        // Then
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Update non-existing category")
    void update_InvalidId_ThrowsNoSuchElementException() {
        // Given
        Long invalidId = 999L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fiction");
        requestDto.setDescription("Fiction books");

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class,
                () -> categoryService.update(invalidId, requestDto));
        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Delete existing category")
    void deleteById_ValidId_DeletesCategory() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Delete non-existing category")
    void deleteById_InvalidId_ThrowsNoSuchElementException() {
        // Given
        Long invalidId = 999L;
        when(categoryRepository.existsById(invalidId)).thenReturn(false);

        // When & Then
        assertThrows(NoSuchElementException.class,
                () -> categoryService.deleteById(invalidId));
        verify(categoryRepository).existsById(invalidId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Find all returns empty list when no categories")
    void findAll_NoCategories_ReturnsEmptyList() {
        // Given
        when(categoryRepository.findAll()).thenReturn(List.of());

        // When
        List<CategoryDto> result = categoryService.findAll();

        // Then
        assertEquals(0, result.size());
        verify(categoryRepository).findAll();
    }
}
