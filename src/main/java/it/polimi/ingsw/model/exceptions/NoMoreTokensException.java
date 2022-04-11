package it.polimi.ingsw.model.exceptions;

public class NoMoreTokensException extends Exception{
    public NoMoreTokensException() {
    }

    public NoMoreTokensException(String message) {
        super(message);
    }
}
