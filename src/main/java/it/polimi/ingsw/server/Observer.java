package it.polimi.ingsw.server;

public interface Observer<T> {

    public void update(T message);

}