package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.intro.config.CustomMySqlContainer;
import mate.academy.intro.dto.AddBookToCartRequestDto;
import mate.academy.intro.dto.CartItemResponseDto;
import mate.academy.intro.dto.ShoppingCartResponseDto;
import mate.academy.intro.dto.UpdateCartItemQuantityRequestDto;
import mate.academy.intro.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
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
class ShoppingCartControllerIntegrationTest {

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

    private TestingAuthenticationToken getAuth() {
        User user = new User();
        user.setId(1L);
        return new TestingAuthenticationToken(user, null);
    }

    @Test
    @DisplayName("GET /cart - returns user's shopping cart")
    @Sql(scripts = "classpath:scripts/insert-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCart_integration() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .principal(getAuth())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class
        );

        assertNotNull(actual);
        assertEquals(1L, actual.getUserId());
        assertNotNull(actual.getCartItems());
        assertTrue(actual.getCartItems().size() > 0);
    }

    @Test
    @DisplayName("POST /cart - add book to shopping cart")
    @Sql(scripts = "classpath:scripts/insert-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBookToCart_integration() throws Exception {
        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(100L);
        requestDto.setQuantity(2);

        MvcResult result = mockMvc.perform(post("/cart")
                        .principal(getAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class
        );

        assertNotNull(actual);
        assertEquals(1L, actual.getUserId());
        assertNotNull(actual.getCartItems());

        boolean found = actual.getCartItems().stream()
                .anyMatch(item -> item.getBookId().equals(100L) && item.getQuantity() == 3);

        assertTrue(found);
    }

    @Test
    @DisplayName("PUT /cart/cart-items/{id} - update cart item quantity")
    @Sql(scripts = "classpath:scripts/insert-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCartItem_integration() throws Exception {
        UpdateCartItemQuantityRequestDto requestDto = new UpdateCartItemQuantityRequestDto();
        requestDto.setQuantity(5);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/10")
                        .principal(getAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CartItemResponseDto.class
        );

        assertNotNull(actual);
        assertEquals(10L, actual.getId());
        assertEquals(5, actual.getQuantity());
    }

    @Test
    @DisplayName("DELETE /cart/cart-items/{id} - remove cart item")
    @Sql(scripts = "classpath:scripts/insert-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:scripts/cleanup.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void removeCartItem_integration() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/10")
                        .principal(getAuth()))
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(get("/cart")
                        .principal(getAuth()))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartResponseDto cart = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class
        );

        boolean exists = cart.getCartItems().stream()
                .anyMatch(item -> item.getId().equals(10L));

        assertFalse(exists);
    }
}
