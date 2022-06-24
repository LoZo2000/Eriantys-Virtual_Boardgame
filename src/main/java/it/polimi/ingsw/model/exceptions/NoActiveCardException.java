package it.polimi.ingsw.model.exceptions;

/**
 * The class NoActiveCardException represent the exception thrown when a card is not active and the player is trying
 * to use his power
 */
public class NoActiveCardException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoActiveCardException() {
    }

    /**
     * This method is the constructor of the class
     * @param message is the message printed out by the exception
     */
    public NoActiveCardException(String message) {
        super(message);
    }
}