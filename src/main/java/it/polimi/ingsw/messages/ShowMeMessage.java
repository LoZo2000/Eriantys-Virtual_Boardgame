package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;

public class ShowMeMessage extends  Message{

    public ShowMeMessage(String sender, Action action){
        super(sender, action);
    }

    @Override
    public Update execute(Game game) {
        return null;
    }
}