package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

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
public class Author {

    @Id
    @Column(name = "author_id", nullable = false)
    private UUID authorId = UUID.randomUUID();

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "{validation.author.firstName.NotBlank}")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "{validation.author.lastName.NotBlank}")
    private String lastName;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public void addBook(Book newBook) {
            books.add(newBook);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId.equals(author.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }
}