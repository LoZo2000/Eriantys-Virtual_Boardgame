package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class AddMeMessage extends Message{
    final private boolean completeRules;
    final private int maxPlayers;

    public AddMeMessage(String sender, Action action, boolean completeRules, int maxPlayers){
        super(sender, action);
        this.completeRules = completeRules;
        this.maxPlayers = maxPlayers;
    }

    public boolean getCompleteRules(){
        return completeRules;
    }
    public int getNumPlayers(){
        return maxPlayers;
    }

}
