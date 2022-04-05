package it.polimi.ingsw.model.exceptions;

public class NoPlayerException extends Exception{
    public NoPlayerException(){
        super();
    }
    public NoPlayerException(String s){
        super(s);
    }
}
