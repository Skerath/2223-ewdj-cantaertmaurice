package domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.ProxyUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@ToString
@Slf4j
@NamedQueries({
        @NamedQuery(
                name = "Book.getStarsForBooks",
                query = "SELECT fb.bookId, COUNT(*) " +
                        "FROM User u " +
                        "JOIN u.favoriteBooks fb " +
                        "GROUP BY fb.bookId"
        ),
        @NamedQuery(
                name = "Book.getStarsForBook",
                query = "SELECT COUNT(*) " +
                        "FROM User u " +
                        "JOIN u.favoriteBooks fb " +
                        "WHERE fb.bookId = :bookId"
        ),
        @NamedQuery(name = "Book.findAllBooksOrderedByName",
                query = "SELECT b FROM Book b ORDER BY b.name"
        ),
        @NamedQuery(name = "Book.getBookFromIsbn",
                query = "SELECT b FROM Book b WHERE b.bookId = :isbn"),
        @NamedQuery(
                name = "Book.findBooksByAuthorName",
                query = "SELECT a.books " +
                        "FROM Author a " +
                        "WHERE CONCAT(a.firstName, ' ', a.lastName) = :authorName"
        )})
public class Book implements Serializable {

    @Id
    @Column(name = "book_id", nullable = false)
    private UUID bookId = UUID.randomUUID();

    @Column(name = "name", nullable = false)
    @NotBlank(message = "{validation.book.name.NotBlank}")
    private String name;

    @ManyToMany
    @JoinTable(name = "author_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @ToString.Exclude
    @Size(min = 1, max = 3, message = "{validation.book.authors.Size}")
    @Valid
    private List<Author> authors = new ArrayList<>();

    @Column(name = "isbn_13", nullable = false, unique = true)
    @NotBlank(message = "{validation.book.isbn13.NotBlank}")
    private String isbn13;

    @Column(name = "price_in_euro", precision = 2)
    @Min(value = 1, message = "{validation.book.priceInEuro.Min}")
    @Max(value = 99, message = "{validation.book.priceInEuro.Max}")
    private BigDecimal priceInEuro;

    @ToString.Exclude
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 1, max = 3, message = "{validation.book.bookLocations.Size}")
    @Valid
    private List<BookLocation> bookLocations = new ArrayList<>();

    public void addBookLocation(BookLocation location) {
        bookLocations.add(location);
    }

    public void removeBookLocation(BookLocation location) {
        bookLocations.remove(location);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o)) return false;
        Book book = (Book) o;
        return getBookId() != null && Objects.equals(getBookId(), book.getBookId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
