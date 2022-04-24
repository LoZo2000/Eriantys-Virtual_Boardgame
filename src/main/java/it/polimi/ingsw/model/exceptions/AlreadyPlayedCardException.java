package it.polimi.ingsw.model.exceptions;

public class AlreadyPlayedCardException extends Exception{
    public AlreadyPlayedCardException() {
    }

    public AlreadyPlayedCardException(String message) {
        super(message);
    }
}
