package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class UserAuthorities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    @ToString.Exclude
    private User user;

    @NotNull
    private String authority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserAuthorities that = (UserAuthorities) o;
        return getUser() != null && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}