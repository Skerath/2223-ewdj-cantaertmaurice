package dto;

import domain.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO {
    private String name;
    private String isbn13;
    private BigDecimal priceInEuro;
    private List<String> authorNames;
    private List<BookLocationDTO> bookLocations;

    public BookDTO(Book book, List<BookLocationDTO> bookLocationDTOs) {
        setName(book.getName());
        setIsbn13(book.getIsbn13());
        setPriceInEuro(book.getPriceInEuro() == null ? new BigDecimal("0") : book.getPriceInEuro());
        setAuthorNames(book.getAuthors().stream()
                .map(author -> author.getFirstName() + " " + author.getLastName())
                .toList());
        setBookLocations(bookLocationDTOs);
    }
}

