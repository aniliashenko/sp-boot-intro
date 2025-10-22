package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.service.BookService;
import mate.academy.intro.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category Management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found categories",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))})
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<CategoryDto> getAllCategories() {
        return categoryService.findAll();
    }

    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(summary = "Delete a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404",
                    description = "Category not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found books",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(
                                    implementation = BookDtoWithoutCategoryIds.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Category not found", content = @Content)
    })
    @GetMapping("/{id}/books")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategory(@PathVariable Long id) {
        return bookService.getBooksByCategoryId(id);
    }
}
