package mate.academy.intro.repository.book;

import mate.academy.intro.model.Book;
import mate.academy.intro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String params) {
        String[] authors = params.split(",");
        return (root, query, criteriaBuilder)
                -> root.get("author").in((Object[]) authors);
    }
}
