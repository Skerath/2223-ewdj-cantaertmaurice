package exception;

import java.io.Serial;

public class AuthorNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AuthorNotFoundException(String authorName) {
        super(String.format("Could not find Author with id %s", authorName));
    }
}
