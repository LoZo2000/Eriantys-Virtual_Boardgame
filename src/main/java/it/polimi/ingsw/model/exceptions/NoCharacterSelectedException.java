package it.polimi.ingsw.model.exceptions;

public class NoCharacterSelectedException extends Exception{
    public NoCharacterSelectedException() {
    }

    public NoCharacterSelectedException(String message) {
        super(message);
    }
}
