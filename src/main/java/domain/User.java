package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.util.ProxyUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
@NamedQuery(
        name = "User.getFavoriteBookIds",
        query = "SELECT fb.bookId " +
                "FROM User u " +
                "JOIN u.favoriteBooks fb " +
                "WHERE fb.id=:userId"
)
@NamedQuery(
        name = "User.getBookIsFavorited",
        query = "SELECT CASE WHEN COUNT(fb) > 0 THEN true ELSE false END " +
                "FROM User u " +
                "JOIN u.favoriteBooks fb " +
                "WHERE u.userId = :userId AND fb.bookId = :bookId"
)
@NamedQuery(
        name = "User.getFavoriteListSize",
        query = "SELECT COUNT(fb) " +
                "FROM User u " +
                "JOIN u.favoriteBooks fb " +
                "WHERE u.userId = :userId"
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank
    @NotEmpty
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    @NotEmpty
    private String password;

    @Column(name = "favorite_books_limit", nullable = false)
    private int favoriteBooksLimit;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_favoritebooks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> favoriteBooks = new ArrayList<>();

    @Column(name = "enabled", nullable = false)
    private final boolean enabled = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserAuthorities> listAuthorities = new ArrayList<>();


    public boolean isFavoriteLimited() {
        return getFavoriteBooks().size() == getFavoriteBooksLimit();
    }

    public void addFavouriteBook(Book book) {
        getFavoriteBooks().add(book);
    }

    public void removeFavouriteBook(Book book) {
        getFavoriteBooks().remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o))
            return false;
        User user = (User) o;
        return getUserId() != null && Objects.equals(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "userId = " + userId + ", " +
                "username = " + username + ", " +
                "favoriteBooksLimit = " + favoriteBooksLimit + ")";
    }
}
