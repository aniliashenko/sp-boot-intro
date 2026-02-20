package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.dto.UpdateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds")
    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(UpdateBookRequestDto book, @MappingTarget Book entity);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {

    }

    default Set<Category> mapCategoryIdsToCategories(List<Long> categoryIds) {
        if (categoryIds == null) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> {
                    Category c = new Category();
                    c.setId(id); // только айди, остальные поля подтянет JPA
                    return c;
                })
                .collect(Collectors.toSet());
    }
}
