package it.polimi.ingsw.model.exceptions;

public class UnrecognizedPlayerOrActionException extends Exception{
    public UnrecognizedPlayerOrActionException(){
        super();
    }
    public UnrecognizedPlayerOrActionException(String s){
        super(s);
    }
}
