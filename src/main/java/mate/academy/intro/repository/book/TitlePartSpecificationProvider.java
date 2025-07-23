package mate.academy.intro.repository.book;

import mate.academy.intro.model.Book;
import mate.academy.intro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitlePartSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String params) {
        String[] titles = params.split(",");
        return (root, query, criteriaBuilder)
                -> root.get("title").in((Object[]) titles);
    }
}
