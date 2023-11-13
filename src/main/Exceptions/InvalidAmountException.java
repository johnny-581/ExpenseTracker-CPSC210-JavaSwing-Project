package Exceptions;

public class InvalidAmountException extends InvalidInputException {
    public InvalidAmountException() {
        super("invalid amount!");
    }
}
