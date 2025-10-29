package mate.academy.intro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
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
class ShoppingCartRepositoryTest {

    @Container
    private static final CustomMySqlContainer mysqlContainer = CustomMySqlContainer.getInstance();

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    @DisplayName("Save and find shopping cart by ID")
    void saveAndFindShoppingCart() {
        User user = createTestUser("user@example.com");
        userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);

        Optional<ShoppingCart> foundCart = shoppingCartRepository.findById(savedCart.getId());

        assertTrue(foundCart.isPresent());
        assertEquals(user.getId(), foundCart.get().getUser().getId());
    }

    @Test
    @DisplayName("Find shopping cart by user entity")
    void findByUser() {
        User user = createTestUser("user2@example.com");
        userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);

        Optional<ShoppingCart> foundCart = shoppingCartRepository.findByUser(user);
        assertTrue(foundCart.isPresent());
        assertEquals(user.getId(), foundCart.get().getUser().getId());
    }

    @Test
    @DisplayName("Find shopping cart by user ID")
    void findByUserId() {
        User user = createTestUser("user3@example.com");
        userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);

        Optional<ShoppingCart> foundCart = shoppingCartRepository.findByUserId(user.getId());
        assertTrue(foundCart.isPresent());
        assertEquals(user.getId(), foundCart.get().getUser().getId());
    }

    @Test
    @DisplayName("Delete shopping cart by ID")
    void deleteShoppingCartById() {
        User user = createTestUser("user4@example.com");
        userRepository.save(user);

        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        ShoppingCart savedCart = shoppingCartRepository.save(cart);

        Long cartId = savedCart.getId();
        shoppingCartRepository.deleteById(cartId);

        assertFalse(shoppingCartRepository.findById(cartId).isPresent());
    }

    private User createTestUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        user.setFirstName("First");
        user.setLastName("Last");
        return user;
    }
}
