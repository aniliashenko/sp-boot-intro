package mate.academy.intro.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);

        registry.add("spring.liquibase.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.main.allow-bean-definition-overriding", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /categories/{id} - returns a category from DB")
    @Sql(scripts = "classpath:scripts/insert-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_integration() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        CategoryDto dto = objectMapper.readValue(json, CategoryDto.class);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Fiction");
    }

    @Test
    @DisplayName("GET /categories - returns a list of categories")
    @Sql(scripts = "classpath:scripts/insert-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllCategories_integration() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<CategoryDto> list = objectMapper.readValue(json, new TypeReference<>() {});

        assertThat(list).isNotEmpty();
        assertThat(list.stream().anyMatch(c -> "Fiction".equals(c.getName()))).isTrue();
    }

    @Test
    @DisplayName("POST /categories - creates a category")
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        CategoryDto created = objectMapper.readValue(json, CategoryDto.class);

        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("NewCat");
        assertThat(created.getId()).isNotNull();
    }

    @Test
    @DisplayName("PUT /categories/{id} - updates category")
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        CategoryDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), CategoryDto.class);
        Long id = created.getId();

        CategoryRequestDto updateReq = new CategoryRequestDto();
        updateReq.setName("UpdatedName");
        updateReq.setDescription("new");

        MvcResult updateResult = mockMvc.perform(put("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto updated = objectMapper.readValue(updateResult.getResponse().getContentAsString(), CategoryDto.class);
        assertThat(updated.getName()).isEqualTo("UpdatedName");
    }
    @Test
    @DisplayName("DELETE /categories/{id} - deletes category")
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

        CategoryDto created = objectMapper.readValue(createResult.getResponse().getContentAsString(), CategoryDto.class);
        Long id = created.getId();

        mockMvc.perform(delete("/categories/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /categories/{id}/books - returns books by category")
    @Sql(scripts = "classpath:scripts/insert-books-with-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategory_integration() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(json, new TypeReference<>() {});

        assertThat(books).isNotEmpty();
        assertThat(books.size()).isEqualTo(2);
        assertThat(books.stream().anyMatch(b -> "Book A".equals(b.getTitle()))).isTrue();
    }
}
