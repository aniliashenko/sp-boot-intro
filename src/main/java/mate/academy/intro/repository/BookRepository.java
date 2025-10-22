package mate.academy.intro.repository;

import java.util.List;
import mate.academy.intro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategories_Id(Long categoryId);
}
