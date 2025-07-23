package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);

    void deleteBookById(Long id);
}
