package it.polimi.ingsw.model.exceptions;

/**
 * The class OverflowCardException represent the exception thrown when a player tries to use an assistant card not in the hand
 */
public class OverflowCardException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public OverflowCardException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public OverflowCardException(String s){
        super(s);
    }
}