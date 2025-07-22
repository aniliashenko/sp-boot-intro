package mate.academy.intro.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.model.Book;

public interface BookRepository {
    Book createBook(Book book);

    List<Book> getAll();

    Optional<Book> getBookById(Long id);
}
