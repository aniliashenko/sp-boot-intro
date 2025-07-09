package mate.academy.intro.repository.impl;

import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
