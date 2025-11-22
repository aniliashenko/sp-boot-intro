package mate.academy.intro.repository;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.BookSearchParametersDto;
import mate.academy.intro.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto parametersDto) {
        Specification<Book> specification = Specification.where(null);

        if (parametersDto.author() != null && !parametersDto.author().isBlank()) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(parametersDto.author()));
        }

        if (parametersDto.titlePart() != null && !parametersDto.titlePart().isBlank()) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider("titlePart")
                    .getSpecification(parametersDto.titlePart()));
        }

        return specification;
    }
}
