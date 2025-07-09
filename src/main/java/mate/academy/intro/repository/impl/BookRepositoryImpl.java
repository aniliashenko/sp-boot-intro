package mate.academy.intro.repository.impl;

import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List findall() {
        return List.of();
    }
}
