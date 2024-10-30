package core.exceptions.adapters;

public class BadRequestException extends Exception {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String exceptionDescription) {
        super(exceptionDescription);
    }
}