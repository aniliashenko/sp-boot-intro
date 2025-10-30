package mate.academy.intro.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
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
    @DisplayName("Find all books by category ID")
    @Sql(scripts = "classpath:scripts/insert-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategories_Id() {
        List<Book> books = bookRepository.findAllByCategories_Id(1L);

        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(2);
        assertThat(books).anyMatch(b -> b.getTitle().equals("Book A"));
        assertThat(books).anyMatch(b -> b.getTitle().equals("Book B"));
    }

    @Test
    @DisplayName("Save and find book by ID")
    void saveAndFindBook() {
        Book newBook = new Book();
        newBook.setTitle("Test Book");
        newBook.setAuthor("Test Author");
        newBook.setIsbn("TEST-ISBN-123");
        newBook.setPrice(BigDecimal.valueOf(29.99));
        newBook.setDescription("Test Description");
        newBook.setCoverImage("test.jpg");

        Book savedBook = bookRepository.save(newBook);
        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Test Book");
    }

    @Test
    @DisplayName("Delete book by ID")
    void deleteBookById() {
        Book book = new Book();
        book.setTitle("To Delete");
        book.setAuthor("Author");
        book.setIsbn("DEL-ISBN");
        book.setPrice(BigDecimal.valueOf(15.99));
        book.setDescription("Desc");
        book.setCoverImage("del.jpg");

        Book saved = bookRepository.save(book);
        Long id = saved.getId();

        bookRepository.deleteById(id);
        assertThat(bookRepository.findById(id)).isEmpty();
    }
}
