package it.polimi.ingsw.controller.exceptions;

public class IllegalMessageException extends Exception {
    public IllegalMessageException(){
        super();
    }
    public IllegalMessageException(String s){
        super(s);
    }
}
