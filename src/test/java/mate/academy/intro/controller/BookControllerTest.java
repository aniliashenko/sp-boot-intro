package mate.academy.intro.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.dto.UpdateBookRequestDto;
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
    @DisplayName("GET /books/{id} - return existing book")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_validId_returnsBook() throws Exception {
        // when
        MvcResult result = mockMvc.perform(get("/books/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getTitle()).isEqualTo("Book One");
        assertThat(actual.getAuthor()).isEqualTo("Author A");
        assertThat(actual.getIsbn()).isEqualTo("ISBN-001");
        assertThat(actual.getPrice()).isEqualByComparingTo("19.99");
        assertThat(actual.getDescription()).isEqualTo("Description One");
        assertThat(actual.getCoverImage()).isEqualTo("cover1.jpg");
    }

    @Test
    @DisplayName("GET /books/{id} - return 404 if not found")
    void getBookById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/books/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /books - returns list of books")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_returnsBooksList() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        assertThat(json).contains("Book One", "Book Two");
    }

    @Test
    @DisplayName("POST /books - creates new book")
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_validRequest_returnsCreatedBook() throws Exception {
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

        BookDto created = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertThat(created.getTitle()).isEqualTo("Integration Book");
        assertThat(created.getAuthor()).isEqualTo("Integration Author");
        assertThat(created.getIsbn()).isEqualTo("INT-ISBN-1");
    }

    @Test
    @DisplayName("POST /books - invalid request returns 400")
    void createBook_invalidRequest_returnsBadRequest() throws Exception {
        CreateBookRequestDto invalid = new CreateBookRequestDto();
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /books/{id} - updates existing book")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_validRequest_returnsUpdatedBook() throws Exception {
        UpdateBookRequestDto updateReq = new UpdateBookRequestDto();
        updateReq.setTitle("Updated Title");
        updateReq.setAuthor("Updated Author");
        updateReq.setIsbn("ISBN-001");
        updateReq.setPrice(BigDecimal.valueOf(19.99));

        MvcResult result = mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        assertThat(actual.getTitle()).isEqualTo("Updated Title");
        assertThat(actual.getAuthor()).isEqualTo("Updated Author");
        assertThat(actual.getPrice()).isEqualByComparingTo("19.99");
    }

    @Test
    @DisplayName("PUT /books/{id} - non-existing id returns 404")
    void updateBook_notFound_returns404() throws Exception {
        UpdateBookRequestDto updateReq = new UpdateBookRequestDto();
        updateReq.setTitle("No such book");
        updateReq.setAuthor("Someone");
        updateReq.setIsbn("999-999");
        updateReq.setPrice(BigDecimal.valueOf(9.99));

        mockMvc.perform(put("/books/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /books/{id} - deletes existing book")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_validId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /books/{id} - non-existing id returns 404")
    void deleteBook_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /books/search - returns filtered books")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_returnsFilteredList() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("author", "Author A")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        assertThat(json).contains("Book One");
        assertThat(json).doesNotContain("Book Two");
    }
}
