package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.GameStatus;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

public abstract class View extends Observable<Message> implements Observer<GameReport> {

    protected View(){}

    protected void processChoice(Message message){
        notify(message);
    }

    protected abstract void showModel(GameStatus GS);
    protected abstract void showModel(GameReport gr);

    @Override
    public void update(GameReport gr) {
        showModel(gr);
    }
}