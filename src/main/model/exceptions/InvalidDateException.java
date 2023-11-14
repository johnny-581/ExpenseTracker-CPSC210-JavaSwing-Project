package model.exceptions;

public class InvalidDateException extends InvalidInputException {
    public InvalidDateException() {
        super("invalid date!");
    }
}
