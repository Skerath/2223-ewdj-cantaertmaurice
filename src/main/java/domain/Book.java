package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.util.ProxyUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id", nullable = false)
    @ToString.Exclude
    private UUID bookId;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @ManyToMany(mappedBy = "books")
    @ToString.Exclude
    @Size(min = 1, max = 3)
    private Set<Author> authors = new LinkedHashSet<>();

    @Column(name = "isbn_13", nullable = false, unique = true)
    private @Setter(AccessLevel.NONE) String isbn13;

    @Column(name = "price_in_euro", precision = 2, scale = 2)
    @Min(1) @Max(99)
    private Double priceInEuro;

    @Formula(value = "(SELECT count(*) FROM user_favoritebooks " +
            "WHERE user_favoritebooks.book_id=book_id)")
    @Column(name = "stars", nullable = false)
    private @Setter(AccessLevel.NONE) int stars;

    @ToString.Exclude
    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @Size(min = 1, max = 3)
    private Set<BookLocation> bookLocations = new LinkedHashSet<>();

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
