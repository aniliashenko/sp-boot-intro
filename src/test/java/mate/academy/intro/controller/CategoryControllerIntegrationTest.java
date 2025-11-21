package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.dto.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CategoryRequestDto;
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
class CategoryControllerIntegrationTest {

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
    @DisplayName("GET /categories/{id} - returns a category from DB")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_integration() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Fiction");
        expected.setDescription(null);

        MvcResult result = mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CategoryDto actual = objectMapper.readValue(json, CategoryDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("GET /categories - returns a list of categories")
    @Sql(scripts = "classpath:scripts/insert-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_integration() throws Exception {
        CategoryDto expected1 = new CategoryDto();
        expected1.setId(1L);
        expected1.setName("Fiction");
        expected1.setDescription(null);

        List<CategoryDto> expected = List.of(expected1);

        MvcResult result = mockMvc.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<CategoryDto> actual = objectMapper.readValue(json, new TypeReference<>() {});
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("POST /categories - creates a category")
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_integration() throws Exception {
        CategoryRequestDto req = new CategoryRequestDto();
        req.setName("NewCat");
        req.setDescription("desc");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CategoryDto actual = objectMapper.readValue(json, CategoryDto.class);

        CategoryDto expected = new CategoryDto();
        expected.setId(actual.getId());
        expected.setName("NewCat");
        expected.setDescription("desc");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("PUT /categories/{id} - updates category")
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_integration() throws Exception {
        CategoryRequestDto createReq = new CategoryRequestDto();
        createReq.setName("ToUpdate");
        createReq.setDescription("old");

        MvcResult createResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto created = objectMapper.readValue(createResult
                .getResponse().getContentAsString(), CategoryDto.class);
        Long id = created.getId();

        CategoryRequestDto updateReq = new CategoryRequestDto();
        updateReq.setName("UpdatedName");
        updateReq.setDescription("new");

        CategoryDto expected = new CategoryDto();
        expected.setId(id);
        expected.setName("UpdatedName");
        expected.setDescription("new");

        MvcResult updateResult = mockMvc.perform(put("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(updateResult
                .getResponse().getContentAsString(), CategoryDto.class);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("DELETE /categories/{id} - deletes category")
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_integration() throws Exception {
        CategoryRequestDto createReq = new CategoryRequestDto();
        createReq.setName("ToDelete");
        createReq.setDescription("desc");

        MvcResult createResult = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto created = objectMapper.readValue(createResult
                .getResponse().getContentAsString(), CategoryDto.class);
        Long id = created.getId();

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /categories/{id}/books - returns books by category")
    @Sql(scripts = "classpath:scripts/insert-books-with-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategory_integration() throws Exception {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setTitle("Book A");
        book1.setAuthor("Author A");
        book1.setIsbn("ISBN-AAA");
        book1.setPrice(new BigDecimal("10.00"));
        book1.setDescription("Desc A");
        book1.setCoverImage("a.jpg");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setTitle("Book B");
        book2.setAuthor("Author B");
        book2.setIsbn("ISBN-BBB");
        book2.setPrice(new BigDecimal("12.00"));
        book2.setDescription("Desc B");
        book2.setCoverImage("b.jpg");

        List<BookDtoWithoutCategoryIds> expected = List.of(book1, book2);

        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(json,
                new TypeReference<>() {});
        assertEquals(expected, actual);
    }
}
