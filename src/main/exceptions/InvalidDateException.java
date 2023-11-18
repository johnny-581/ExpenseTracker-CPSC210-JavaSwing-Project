package exceptions;

public class InvalidDateException extends InvalidInputException {
    public InvalidDateException() {
        super("invalid date!");
    }
}
