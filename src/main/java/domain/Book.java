package domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.util.ProxyUtils;
import org.springframework.format.annotation.NumberFormat;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id", nullable = false)
    @ToString.Exclude
    private UUID bookId = UUID.randomUUID();

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "books")
    @ToString.Exclude
    @Size(min = 1, max = 3)
    @Valid
    private List<Author> authors = new ArrayList<>();

    @Column(name = "isbn_13", nullable = false, unique = true)
    @NotBlank
    private String isbn13;

    @Column(name = "price_in_euro", precision = 2, scale = 2)
    @Min(1) @Max(99)
    @NumberFormat(pattern = "#.00")
    private Double priceInEuro;

    @Formula(value = "(SELECT count(*) FROM user_favoritebooks " +
            "WHERE user_favoritebooks.book_id=book_id)")
    @Column(name = "stars", nullable = false)
    private @Setter(AccessLevel.NONE) int stars = 0;

    @ToString.Exclude
    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @Size(min = 1, max = 3)
    @Valid
    private List<BookLocation> bookLocations = new ArrayList<>();

    public void addBookLocation(BookLocation location) {
        bookLocations.add(location);
    }

    public void removeBookLocation(BookLocation location) {
        bookLocations.add(location);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(Author author) {
        authors.add(author);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o))
            return false;
        Book book = (Book) o;
        return getBookId() != null && Objects.equals(getBookId(), book.getBookId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
