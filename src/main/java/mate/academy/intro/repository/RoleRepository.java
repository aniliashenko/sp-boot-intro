package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {
    //@Query(nativeQuery = true, value = "SELECT r FROM roles AS r WHERE r.roleName LIKE :roleName")
    Optional<Role> findByRoleName(@Param("roleName") Role.RoleName roleName);
}
