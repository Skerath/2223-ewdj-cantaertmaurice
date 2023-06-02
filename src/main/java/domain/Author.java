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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "author_id", nullable = false)
    private UUID authorId;

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
        boolean updated = true;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(newBook)) {
                log.error("ping");
                books.set(i, newBook);
                updated = false;
                break;
            }
        }
        if (!updated)
            books.add(newBook);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return getAuthorId() != null && Objects.equals(getAuthorId(), author.getAuthorId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}