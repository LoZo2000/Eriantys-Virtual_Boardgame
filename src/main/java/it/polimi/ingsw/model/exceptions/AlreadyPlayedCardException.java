package it.polimi.ingsw.model.exceptions;

/**
 * The class AlreadyPlayedCardException represent the exception thrown when a player try to play a card already
 * played (according to the rules)
 */
public class AlreadyPlayedCardException extends Exception{

    /**
     * This method is a constructor of the class
     */
    public AlreadyPlayedCardException() {
    }

    /**
     * This method is a constructor of the class
     * @param message is the message that will be printed out
     */
    public AlreadyPlayedCardException(String message) {
        super(message);
    }
}
