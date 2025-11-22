package mate.academy.intro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Container
    private static final CustomMySqlContainer mysqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    @DisplayName("Save and find category by ID")
    void saveAndFindCategory() {
        Category newCategory = createTestCategory("Fiction", "Fiction books");

        Category savedCategory = categoryRepository.save(newCategory);
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals("Fiction", foundCategory.get().getName());
        assertEquals("Fiction books", foundCategory.get().getDescription());
    }

    @Test
    @DisplayName("Find all categories")
    void findAllCategories() {
        categoryRepository.save(createTestCategory("Fiction", "Fiction books"));
        categoryRepository.save(createTestCategory("Science", "Science books"));

        List<Category> allCategories = categoryRepository.findAll();

        assertEquals(2, allCategories.size());
        assertTrue(allCategories.stream().anyMatch(c -> c.getName().equals("Fiction")));
        assertTrue(allCategories.stream().anyMatch(c -> c.getName().equals("Science")));
    }

    @Test
    @DisplayName("Delete category by ID")
    void deleteCategoryById() {
        Category savedCategory = categoryRepository.save(createTestCategory(
                "To Delete", "To be deleted"));
        Long id = savedCategory.getId();

        categoryRepository.deleteById(id);

        Optional<Category> deletedCategory = categoryRepository.findById(id);
        assertFalse(deletedCategory.isPresent());
    }

    @Test
    @DisplayName("Find category by name")
    void findCategoryByName() {
        Category savedCategory = categoryRepository.save(createTestCategory(
                "Fantasy", "Fantasy books"));

        Optional<Category> foundCategory = categoryRepository.findAll()
                .stream()
                .filter(c -> c.getName().equals("Fantasy"))
                .findFirst();

        assertTrue(foundCategory.isPresent());
        assertEquals(savedCategory.getId(), foundCategory.get().getId());
        assertEquals("Fantasy books", foundCategory.get().getDescription());
    }

    @Test
    @DisplayName("Update category description")
    void updateCategory() {
        Category category = categoryRepository.save(createTestCategory(
                "Tech", "Technical books"));

        category.setDescription("Updated technical books");
        Category updatedCategory = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(updatedCategory.getId());
        assertTrue(foundCategory.isPresent());
        assertEquals("Updated technical books", foundCategory.get().getDescription());
    }

    @Test
    @DisplayName("Check unique name constraint")
    void checkUniqueName() {
        Category category1 = createTestCategory("Unique",
                "First category");
        Category category2 = createTestCategory("Unique",
                "Second category with same name");

        categoryRepository.save(category1);

        try {
            categoryRepository.saveAndFlush(category2);
        } catch (Exception e) {
            assertTrue(e instanceof org.springframework.dao.DataIntegrityViolationException);
        }
    }

    private Category createTestCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setDeleted(false);
        return category;
    }
}
