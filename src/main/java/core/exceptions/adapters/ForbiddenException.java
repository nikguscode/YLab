package core.exceptions.adapters;

public class ForbiddenException extends Exception {
    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String exceptionDescription) {
        super(exceptionDescription);
    }
}