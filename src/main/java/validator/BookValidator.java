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

        String isbn13 = filterISBN13(registration.getIsbn13());
        if (!validateChecksum(isbn13)) {
            errors.rejectValue("isbn13", "invalidISBN",
                    "{invalidISBN13.book.isbn13}");
            return;
        }
        registration.setIsbn13(isbn13);
    }

    private String filterISBN13(String isbn) {
        return isbn.replaceAll("[^\\d]", ""); // TODO na testen: can be simplified
    }

    private boolean validateChecksum(String isbn) {
        String filteredISBN = filterISBN13(isbn);

        if (filteredISBN.length() != 13)
            return false;

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Integer.parseInt(filteredISBN.substring(i, i + 1));
            sum += (i % 2 == 0) ? digit : (digit * 3);
        }
        int checksum = 10 - (sum % 10);
        checksum = (checksum == 10) ? 0 : checksum;

        return checksum == Integer.parseInt(filteredISBN.substring(filteredISBN.length() - 1));
    }

}
