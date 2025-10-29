package mate.academy.intro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Container
    private static final CustomMySqlContainer mysqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    @DisplayName("Save and find book by ID")
    void saveAndFindBook() {
        Book newBook = createTestBook("Test Book",
                "Test Author", "TEST-ISBN-123");

        Book savedBook = bookRepository.save(newBook);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
    }

    @Test
    @DisplayName("Find all books")
    void findAllBooks() {
        bookRepository.save(createTestBook("Book 1",
                "Author 1", "ISBN-111"));
        bookRepository.save(createTestBook("Book 2",
                "Author 2", "ISBN-222"));

        List<Book> allBooks = bookRepository.findAll();

        assertEquals(2, allBooks.size());
        assertTrue(allBooks.stream().anyMatch(
                b -> b.getTitle().equals("Book 1")));
    }

    @Test
    @DisplayName("Delete book by ID")
    void deleteBookById() {
        Book savedBook = bookRepository.save(createTestBook("To Delete",
                "Author", "DEL-ISBN"));
        Long id = savedBook.getId();

        bookRepository.deleteById(id);

        assertFalse(bookRepository.findById(id).isPresent());
    }

    @Test
    @DisplayName("Find books by category ID using @Sql")
    @Sql(statements = {
            "INSERT INTO categories (id, name, is_deleted) VALUES (1, 'Fiction', false);",
            "INSERT INTO books (id, title, author, isbn, price, "
                    + "description, cover_image, is_deleted) "
                    + "VALUES (100, 'Book A', 'Author A', 'ISBN-AAA',"
                    + " 10.00, 'Desc', 'a.jpg', false);",
            "INSERT INTO books (id, title, author, isbn, price,"
                    + " description, cover_image, is_deleted) "
                    + "VALUES (200, 'Book B', 'Author B', 'ISBN-BBB',"
                    + " 12.00, 'Desc', 'b.jpg', false);",
            "INSERT INTO books_categories (book_id, category_id) VALUES (100, 1);"
    })
    @Sql(statements = {
            "DELETE FROM books_categories;",
            "DELETE FROM books;",
            "DELETE FROM categories;"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategories_Id() {
        List<Book> books = bookRepository.findAllByCategories_Id(1L);

        assertEquals(1, books.size());
        assertEquals("Book A", books.get(0).getTitle());
    }

    private Book createTestBook(String title, String author, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setDescription("Test Description");
        book.setCoverImage("test.jpg");
        return book;
    }
}
