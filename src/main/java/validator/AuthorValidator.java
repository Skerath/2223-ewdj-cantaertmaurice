package validator;

import domain.Author;
import jakarta.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AuthorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @Valid Author registration = (Author) target;

//        double price = registration.getPrice();
//        if (price <= 0 || price >= 100) {
//            errors.rejectValue("price", "priceValueBetween0Aand100",
//                    "if entered, price must be above 0 and below 100 ");
//        }
    }
}
