package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class USEPOWERmessage extends Message{
    private int characterCard;

    public USEPOWERmessage(String sender, Action action, int characterCard) {
        super(sender, action);
        this.characterCard = characterCard;
    }

    public int getCharacterCard(){
        return characterCard;
    }
}
