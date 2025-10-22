package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findByUserId(Long userId);
}
