package validator;

import domain.BookLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public class BookLocationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BookLocation.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookLocation registration = (BookLocation) target;

//        double price = registration.getPrice();
//        if (price <= 0 || price >= 100) {
//            errors.rejectValue("price", "priceValueBetween0Aand100",
//                    "if entered, price must be above 0 and below 100 ");
//        }
    }
}

