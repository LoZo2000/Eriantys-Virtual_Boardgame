package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Observable is on of the two principal component of the Observer Pattern, is the class that managed the observables objects
 * and provide the methods to add observers and notify the observers, this class is implemented by the observables
 * @param <T> is the type of the object passed to the method notify
 */
public class Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    /**
     * This method add and observer to the class
     * @param observer is the observer that will check the class in case of notify
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * This method notify the observers if called
     * @param message is the object passed to the observers
     */
    public void notify(T message){
        synchronized (observers) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }
}