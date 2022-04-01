package it.polimi.ingsw.controller.exceptions;

public class UnrecognizedPlayerOrActionException extends Exception{
    public UnrecognizedPlayerOrActionException(){
        super();
    }
    public UnrecognizedPlayerOrActionException(String s){
        super(s);
    }
}
