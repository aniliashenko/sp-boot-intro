package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.BookSearchParametersDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.dto.UpdateBookRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.BookSpecificationBuilder;
import mate.academy.intro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Create book with valid data")
    void createBook_ValidRequestDto_ReturnsBookDto() {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Test Book");
        requestDto.setAuthor("Test Author");
        requestDto.setIsbn("1234567890");
        requestDto.setPrice(BigDecimal.valueOf(29.99));
        requestDto.setDescription("Test Description");
        requestDto.setCoverImage("test.jpg");

        Book book = new Book();
        book.setTitle("Test Book");

        BookDto expected = new BookDto();
        expected.setTitle("Test Book");
        expected.setAuthor("Test Author");
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.valueOf(29.99));
        expected.setDescription("Test Description");
        expected.setCoverImage("test.jpg");
        expected.setCategoryIds(Set.of(1L, 2L));

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.createBook(requestDto);

        // Then
        assertEquals(expected, actual);
        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Find all books with pagination")
    void findAll_ValidPageable_ReturnsPageOfBookDto() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        book.setTitle("Test Book");
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(BigDecimal.valueOf(29.99));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        Page<BookDto> result = bookService.findAll(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(0));
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get existing book by id")
    void getBookById_ValidId_ReturnsBookDto() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        book.setTitle("Test Book");

        BookDto expected = new BookDto();
        expected.setTitle("Test Book");
        expected.setAuthor("Test Author");
        expected.setIsbn("1234567890");
        expected.setPrice(BigDecimal.valueOf(29.99));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        // When
        BookDto actual = bookService.getBookById(bookId);

        // Then
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get non-existing book by id")
    void getBookById_InvalidId_ThrowsException() {
        // Given
        Long invalidId = 999L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(invalidId));
        verify(bookRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Update existing book")
    void updateBookById_ValidId_ReturnsUpdatedBookDto() {
        // Given
        Long bookId = 1L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setPrice(BigDecimal.valueOf(39.99));
        requestDto.setAuthor("Updated Author");

        Book existingBook = new Book();
        existingBook.setTitle("Original Title");
        existingBook.setAuthor("Original Author");
        existingBook.setPrice(BigDecimal.valueOf(29.99));

        BookDto expected = new BookDto();
        expected.setTitle("Updated Title");
        expected.setAuthor("Updated Author");
        expected.setPrice(BigDecimal.valueOf(39.99));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(expected);

        // When
        BookDto actual = bookService.updateBookById(bookId, requestDto);

        // Then
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(existingBook);
    }

    @Test
    @DisplayName("Update non-existing book")
    void updateBookById_InvalidId_ThrowsException() {
        // Given
        Long invalidId = 999L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBookById(invalidId, requestDto));
        verify(bookRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Delete existing book")
    void deleteBookById_ValidId_DeletesBook() {
        // Given
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteBookById(bookId);

        // Then
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Delete non-existing book")
    void deleteBookById_InvalidId_ThrowsException() {
        // Given
        Long invalidId = 999L;
        when(bookRepository.existsById(invalidId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteBookById(invalidId));
        verify(bookRepository).existsById(invalidId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Search books with parameters")
    void search_ValidParameters_ReturnsFilteredBooks() {
        // Given
        BookSearchParametersDto parametersDto = new BookSearchParametersDto("Title", "Author");
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setTitle("Test Book");
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");

        Specification<Book> specification = (root, query, criteriaBuilder) -> null;

        when(bookSpecificationBuilder.build(parametersDto)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        Page<BookDto> result = bookService.search(parametersDto, pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto, result.getContent().get(0));
        verify(bookSpecificationBuilder).build(parametersDto);
        verify(bookRepository).findAll(specification, pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("Get books by category id")
    void getBooksByCategoryId_ValidCategoryId_ReturnsBooksWithoutCategoryIds() {
        // Given
        Long categoryId = 1L;
        Book book1 = new Book();
        book1.setTitle("Book 1");
        Book book2 = new Book();
        book2.setTitle("Book 2");
        List<Book> books = List.of(book1, book2);

        BookDtoWithoutCategoryIds dto1 = new BookDtoWithoutCategoryIds();
        dto1.setTitle("Book 1");
        dto1.setAuthor("Author 1");

        BookDtoWithoutCategoryIds dto2 = new BookDtoWithoutCategoryIds();
        dto2.setTitle("Book 2");
        dto2.setAuthor("Author 2");

        when(bookRepository.findAllByCategories_Id(categoryId)).thenReturn(books);
        when(bookMapper.toDtoWithoutCategories(book1)).thenReturn(dto1);
        when(bookMapper.toDtoWithoutCategories(book2)).thenReturn(dto2);

        // When
        List<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategoryId(categoryId);

        // Then
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(bookRepository).findAllByCategories_Id(categoryId);
        verify(bookMapper, times(2)).toDtoWithoutCategories(any(Book.class));
    }
}
