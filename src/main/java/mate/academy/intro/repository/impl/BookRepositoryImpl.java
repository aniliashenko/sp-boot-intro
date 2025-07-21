package mate.academy.intro.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Book save(Book book) {
        Session session = entityManager.unwrap(Session.class);
        session.persist(book);
        return book;
    }

    @Override
    @Transactional
    public List<Book> findAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Book", Book.class).getResultList();
    }
}
