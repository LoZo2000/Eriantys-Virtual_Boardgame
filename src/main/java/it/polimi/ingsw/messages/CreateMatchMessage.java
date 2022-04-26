package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class CreateMatchMessage extends Message{
    final private boolean completeRules;
    final private int numPlayers;

    public CreateMatchMessage(String sender, Action action, boolean completeRules, int numPlayers){
        super(sender, action);
        this.completeRules = completeRules;
        this.numPlayers = numPlayers;
    }

    public boolean getCompleteRules(){
        return completeRules;
    }
    public int getNumPlayers(){
        return numPlayers;
    }
}
