package mate.academy.intro;

import java.math.BigDecimal;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroApplication {
    private final BookService bookService;

    @Autowired
    public IntroApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            book.setDescription("Description");
            book.setPrice(BigDecimal.valueOf(100));
            book.setIsbn("1111");
            book.setCoverImage("CoverImage");

            Book savedBook = bookService.save(book);
            System.out.println("Saved Book: " + savedBook);

            bookService.findAll().forEach(System.out::println);
        };
    }
}
