package mate.academy.intro.repository;

import mate.academy.intro.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository {
    Book save(Book book);

    List findall();
}
