package it.polimi.ingsw.model.exceptions;

/**
 * The class CannotJoinException represent the exception thrown when a player cannot join the game
 */
public class CannotJoinException extends Exception{
    /**
     * This method is the constructor of the class
     */
    public CannotJoinException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public CannotJoinException(String s){
        super(s);
    }
}