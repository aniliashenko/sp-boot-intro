package mate.academy.intro.repository;

import mate.academy.intro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("UPDATE Book b SET b.title = :title, b.author = :author WHERE b.id = :id")
    Book updateBookById(Long id, Book book);

    @Query("DELETE FROM Book b WHERE b.id = :id")
    Book deleteBookById(Long id);
}
