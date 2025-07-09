package mate.academy.intro.service.impl;

import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public List findAll() {
        return List.of();
    }
}
