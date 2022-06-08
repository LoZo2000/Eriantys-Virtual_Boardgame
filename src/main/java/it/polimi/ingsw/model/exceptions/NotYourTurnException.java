package it.polimi.ingsw.model.exceptions;
/**
 * The class NotYourTurnException represent the exception thrown when a player tries to execute an action during the
 *  turn of another player
 */
public class NotYourTurnException extends Exception{

    /**
     *  This method is the constructor of the class
     */
    public NotYourTurnException(){
        super();
    }

    /**
     * This method is the constructor of the class
     * @param s is the message printed out by the exception
     */
    public NotYourTurnException(String s){
        super(s);
    }
}