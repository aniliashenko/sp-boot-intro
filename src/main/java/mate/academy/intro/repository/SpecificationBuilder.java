package mate.academy.intro.repository;

import mate.academy.intro.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParametersDto parametersDto);
}
