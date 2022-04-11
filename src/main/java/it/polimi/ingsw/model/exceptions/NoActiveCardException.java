package it.polimi.ingsw.model.exceptions;

public class NoActiveCardException extends Exception{
    public NoActiveCardException() {
    }

    public NoActiveCardException(String message) {
        super(message);
    }
}
