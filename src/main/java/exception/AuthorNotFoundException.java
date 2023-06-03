package exception;

import java.util.UUID;

public class AuthorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthorNotFoundException(UUID id) {
        super(String.format("Could not find Author with id %s", id.toString()));
    }
}
