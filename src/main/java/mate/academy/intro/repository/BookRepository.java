package mate.academy.intro.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import mate.academy.intro.model.Book;

@Repository
public interface BookRepository {
    Book save(Book book);

    List findall();
}
