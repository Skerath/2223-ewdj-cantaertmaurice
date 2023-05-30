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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class BookLocation {

//    BookLocation(Book book, int placeCode1, int placeCode2, String locationName) {
//        if (Math.abs(placeCode1 - placeCode2) < 50) // TODO custom exceptions & custom validation class
//            throw new IllegalArgumentException(String.format("placeCodes total wasn't min 50, min %d max %d total %d", placeCode1, placeCode2, Math.abs(placeCode1 - placeCode2)));
//        this.placeCode1 = placeCode1;
//        this.placeCode2 = placeCode2;
//        this.locationName = locationName;
//    }

    public BookLocation(Book book) {
        setBook(book);
    }

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
    private int placeCode1;

    @Column(name = "place_code_2", nullable = false)
    @Min(50) @Max(300)
    private int placeCode2;

    @Column(name = "location_name", nullable = false)
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]$")
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
