package dto;

import domain.BookLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookLocationDTO {
    private String locationName;
    private int placeCode1;
    private int placeCode2;

    public BookLocationDTO(BookLocation bookLocation) {
        setLocationName(bookLocation.getLocationName());
        setPlaceCode1(bookLocation.getPlaceCode1());
        setPlaceCode2(bookLocation.getPlaceCode2());
    }
}