package it.polimi.ingsw.model.exceptions;

/**
 * The class NoCharacterSelectedException represent the exception thrown when in the method usePower the index assume a wrong value
 */
public class NoCharacterSelectedException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoCharacterSelectedException() {
    }

    /**
     * This method is the constructor of the class
     * @param message is the message printed out by the exception
     */
    public NoCharacterSelectedException(String message) {
        super(message);
    }
}
