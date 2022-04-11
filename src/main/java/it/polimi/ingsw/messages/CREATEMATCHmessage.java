package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Action;

public class CREATEMATCHmessage extends Message{
    final private boolean completeRules;
    final private int numPlayers;

    public CREATEMATCHmessage(String sender, Action action, boolean completeRules, int numPlayers){
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
