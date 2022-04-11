package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.view.Observable;

public class Model extends Observable<Model> implements Cloneable{
    GameHandler gameHandler = null;

    public void setPlayerChoice(Message message){
        if(gameHandler == null && message.getAction()== Action.CREATEMATCH){
            gameHandler = new GameHandler();
        }
        try {
            gameHandler.execute(message);
        }catch(Exception e){
            e.printStackTrace();
        }
        notify(this);
    }

    public Game getGame() {
        return gameHandler.getGame();
    }
}