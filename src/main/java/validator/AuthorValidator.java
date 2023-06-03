package validator;

import domain.Author;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthorValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @Valid Author registration = (Author) target;
    }
}
