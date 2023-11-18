package exceptions;

public class InvalidAmountException extends InvalidInputException {
    public InvalidAmountException() {
        super("invalid amount!");
    }
}
