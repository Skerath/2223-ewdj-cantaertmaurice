package exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookNotFoundException(String isbn) {
        super(String.format("Could not find Book with isbn %s", isbn));
    }
}
