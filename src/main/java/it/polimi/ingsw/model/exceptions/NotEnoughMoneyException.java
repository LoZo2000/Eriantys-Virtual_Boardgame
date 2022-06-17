package it.polimi.ingsw.model.exceptions;

/**
 * The class NotEnoughMoneyException represent the exception thrown when a player tries to use a card that require more money
 * than the number owned by the player
 */
public class NotEnoughMoneyException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NotEnoughMoneyException() {
    }

    /**
     * This method is the constructor of the class
     * @param message is the message printed out by the exception
     */
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
