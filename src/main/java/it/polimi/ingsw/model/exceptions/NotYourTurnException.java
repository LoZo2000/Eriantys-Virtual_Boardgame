package it.polimi.ingsw.model.exceptions;

public class NotYourTurnException extends Exception{
    public NotYourTurnException(){
        super();
    }
    public NotYourTurnException(String s){
        super(s);
    }
}