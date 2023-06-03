package validator;

import domain.BookLocation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class BookLocationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BookLocation.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @Valid BookLocation registration = (BookLocation) target;
    }
}

