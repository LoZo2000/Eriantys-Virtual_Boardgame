package it.polimi.ingsw.model.exceptions;

public class EndGameException extends Exception{
    public EndGameException(){
        super();
    }
    public EndGameException(String s){
        super(s);
    }
}