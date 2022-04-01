package it.polimi.ingsw.controller.exceptions;

public class EndGameException extends Exception{
    public EndGameException(){
        super();
    }
    public EndGameException(String s){
        super(s);
    }
}