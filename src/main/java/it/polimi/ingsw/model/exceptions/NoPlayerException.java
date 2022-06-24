package it.polimi.ingsw.model.exceptions;

/**
 * The class NoPlayerException represent the exception thrown when a method doesn't found a player with a specific nickname
 */
public class NoPlayerException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NoPlayerException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NoPlayerException(String s){
        super(s);
    }
}