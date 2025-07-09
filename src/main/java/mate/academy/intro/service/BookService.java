package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.model.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book book);

    List findAll();
}
