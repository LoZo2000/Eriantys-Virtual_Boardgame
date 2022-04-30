package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.GameStatus;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

public abstract class View extends Observable<Message> implements Observer<GameStatus> {

    protected View(){}

    protected void processChoice(Message message){
        notify(message);
    }

    protected abstract void showModel(GameStatus GS);

    @Override
    public void update(GameStatus GS) {
        showModel(GS);
    }
}