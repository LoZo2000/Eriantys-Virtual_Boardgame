package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Game;

public class CreateMatchMessage extends Message{
    final private boolean completeRules;
    final private int numPlayers;

    public CreateMatchMessage(String sender, boolean completeRules, int numPlayers){
        super(sender, Action.CREATEMATCH);
        this.completeRules = completeRules;
        this.numPlayers = numPlayers;
    }

    @Override
    public Update execute(Game game) {
        return null;
    }

    public boolean getCompleteRules(){
        return completeRules;
    }
    public int getNumPlayers(){
        return numPlayers;
    }
}
