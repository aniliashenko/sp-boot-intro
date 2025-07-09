package mate.academy.intro.repository;

import java.util.List;
import mate.academy.intro.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository {
    Book save(Book book);

    List findall();
}
