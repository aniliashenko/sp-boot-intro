package mate.academy.intro.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.service.BookService;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public BookDto createBook(CreateBookRequestDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public BookDto updateBookById(@PathVariable Long id, @RequestBody CreateBookRequestDto requestDto) {
        return bookService.updateBookById(id, requestDto);
    }

    @DeleteMapping
    @ResponseBody
    public BookDto deleteBookById(@PathVariable Long id) {
        return bookService.deleteBookById(id);
    }
}
