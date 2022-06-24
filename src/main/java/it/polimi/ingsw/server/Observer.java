package it.polimi.ingsw.server;

/**
 * This interface is part of the Observer pattern, is implemented by the Observer classes
 * @param <T> is the type of the object received in the method update
 */
public interface Observer<T> {

    /**
     * This method contain the action that every observer will do after being notified, every class that implement the Observer
     * interface will override the update method
     * @param message is the object passed to the update
     */
    public void update(T message);
}