package validator;

import domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.BookRepository;

@Slf4j
@Component
public class BookValidator implements Validator {

    @Autowired
    BookRepository bookRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book registration = (Book) target;

        String isbn13 = filterISBN13(registration.getIsbn13());

        if (isbn13.length() != 13) {
            errors.rejectValue("isbn13", "validation.book.isbn13.InvalidFormat",
                    "Given ISBN13 had an invalid format");
            return;
        }
        if (!validateChecksum(isbn13)) {
            errors.rejectValue("isbn13", "validation.book.isbn13.InvalidCode",
                    "Given ISBN13 was an invalid code");
            return;
        }

        Book existingBook = bookRepository.findBookByIsbn13(isbn13);
        if (existingBook != null && !existingBook.equals(registration)) {
            errors.rejectValue("isbn13", "validation.book.isbn13.Exists",
                    "Given ISBN13 is already registered");
            return;
        }

        registration.setIsbn13(isbn13);
    }

    private String filterISBN13(String isbn) {
        return isbn.replaceAll("[^\\d]", "");
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
