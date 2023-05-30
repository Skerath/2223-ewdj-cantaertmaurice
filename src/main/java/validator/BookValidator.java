package validator;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
public class BookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book registration = (Book) target;

//        double price = registration.getPrice();
//        if (price <= 0 || price >= 100) {
//            errors.rejectValue("price", "priceValueBetween0Aand100",
//                    "if entered, price must be above 0 and below 100 ");
//        }
    }
}
