package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
class BookControllerTest {

    @Container
    private static final CustomMySqlContainer mysqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.liquibase.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.main.allow-bean-definition-overriding", () -> "true");
    }

    @Test
    @DisplayName("GET /books/{id} - return a book")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_integration() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        BookDto expected = new BookDto();
        expected.setTitle("Book One");
        expected.setAuthor("Author A");
        expected.setIsbn("ISBN-001");
        expected.setPrice(new BigDecimal("19.99"));
        expected.setDescription("Description One");
        expected.setCoverImage("cover1.jpg");
        expected.setCategoryIds(Set.of(1L));

        BookDto dto = objectMapper.readValue(json, BookDto.class);
        assertEquals(expected, dto);
    }

    @Test
    @DisplayName("GET /books - returns a list of books")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_integration() throws Exception {
        BookDto book1 = new BookDto();
        book1.setTitle("Book One");
        book1.setAuthor("Author A");
        book1.setIsbn("ISBN-001");
        book1.setPrice(new BigDecimal("19.99"));
        book1.setDescription("Description One");
        book1.setCoverImage("cover1.jpg");
        book1.setCategoryIds(Set.of(1L));

        BookDto book2 = new BookDto();
        book2.setTitle("Book Three");
        book2.setAuthor("Author C");
        book2.setIsbn("ISBN-003");
        book2.setPrice(new BigDecimal("15.99"));
        book2.setDescription("Description Three");
        book2.setCoverImage("cover3.jpg");
        book2.setCategoryIds(Set.of());

        BookDto book3 = new BookDto();
        book3.setTitle("Book Two");
        book3.setAuthor("Author B");
        book3.setIsbn("ISBN-002");
        book3.setPrice(new BigDecimal("25.99"));
        book3.setDescription("Description Two");
        book3.setCoverImage("cover2.jpg");
        book3.setCategoryIds(Set.of());

        List<BookDto> expected = List.of(book1, book2, book3);

        MvcResult result = mockMvc.perform(get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(json);
        JsonNode contentNode = root.get("content");
        List<BookDto> actual = objectMapper.readerForListOf(BookDto.class)
                .readValue(contentNode);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("POST /books - creates a book")
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_integration() throws Exception {
        CreateBookRequestDto createReq = new CreateBookRequestDto();
        createReq.setTitle("Integration Book");
        createReq.setAuthor("Integration Author");
        createReq.setIsbn("INT-ISBN-1");
        createReq.setPrice(BigDecimal.valueOf(9.99));
        createReq.setDescription("desc");
        createReq.setCoverImage("cover.jpg");

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(json, BookDto.class);

        BookDto expected = new BookDto();
        expected.setTitle("Integration Book");
        expected.setAuthor("Integration Author");
        expected.setIsbn("INT-ISBN-1");
        expected.setPrice(BigDecimal.valueOf(9.99));
        expected.setDescription("desc");
        expected.setCoverImage("cover.jpg");
        expected.setCategoryIds(actual.getCategoryIds());

        assertEquals(expected, actual);
    }
}
