package mate.academy.intro.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findall();
    }
}
