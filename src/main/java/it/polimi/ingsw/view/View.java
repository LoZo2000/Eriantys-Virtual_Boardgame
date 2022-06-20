package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

/**
 * This class represent the View in the model view controller pattern, it extends the observable and ia also an observer
 * because the view is observed by the controller and observer the game
 */
public abstract class View extends Observable<Message> implements Observer<GameReport> {

    /**
     * This method is the constructor of the class
     */
    protected View(){}

    /**
     * This method is used to elaborate the message
     * @param message is the message to process
     */
    protected void processChoice(Message message){
        notify(message);
    }

    /**
     * This method is called to send the state of hte model to the view
     * @param gr is the GameReport representing the state of the game (model)
     */
    protected abstract void showModel(GameReport gr);

    /**
     * This method is called when an object observed by the view notify the view it calls the show model method
     * @param gr is the object passed to the update
     */
    @Override
    public void update(GameReport gr) {
        showModel(gr);
    }
}