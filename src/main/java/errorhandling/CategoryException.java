package errorhandling;

import javax.ws.rs.WebApplicationException;

public class CategoryException extends WebApplicationException {

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException() {
        super("Categories not valid");
    }
}
