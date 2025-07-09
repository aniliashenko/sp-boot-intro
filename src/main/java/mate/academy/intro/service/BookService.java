package mate.academy.intro.service;

import mate.academy.intro.model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {
    Book save(Book book);

    List findAll();
}
