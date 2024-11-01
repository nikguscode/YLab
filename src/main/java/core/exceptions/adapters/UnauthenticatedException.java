package core.exceptions.adapters;

public class UnauthenticatedException extends Exception {
    public UnauthenticatedException() {
        super();
    }

    public UnauthenticatedException(String exceptionDescription) {
        super(exceptionDescription);
    }
}
