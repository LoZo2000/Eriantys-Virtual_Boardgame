package it.polimi.ingsw.model.exceptions;

public class NoContiguousIslandException extends Exception {
    public NoContiguousIslandException(){
        super();
    }
    public NoContiguousIslandException(String s){
        super(s);
    }
}
