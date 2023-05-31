package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.util.ProxyUtils;

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
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "author_id", nullable = false)
    @ToString.Exclude
    private UUID authorId = UUID.randomUUID();

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "{validation.author.firstName.NotBlank}")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "{validation.author.lastName.NotBlank}")
    private String lastName;

    @ManyToMany (mappedBy = "authors")
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o))
            return false;
        Author author = (Author) o;
        return getAuthorId() != null && Objects.equals(getAuthorId(), author.getAuthorId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}