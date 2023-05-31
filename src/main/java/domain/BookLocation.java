package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class BookLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "location_id", nullable = false)
    @ToString.Exclude
    private UUID locationId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "place_code_1", nullable = false)
    @Min(50) @Max(300)
    @NotNull(message = "{NotNull.bookLocation.placeCode}")
    private Integer placeCode1;

    @Column(name = "place_code_2", nullable = false)
    @Min(50) @Max(300)
    @NotNull(message = "{NotNull.bookLocation.placeCode}")
    private Integer placeCode2;

    @Column(name = "location_name", nullable = false)
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]+$")
    private String locationName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookLocation that = (BookLocation) o;
        return getLocationId() != null && Objects.equals(getLocationId(), that.getLocationId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
