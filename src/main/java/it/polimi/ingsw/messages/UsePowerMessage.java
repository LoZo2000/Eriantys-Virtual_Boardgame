package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class UsePowerMessage extends Message{
    private final int characterCard;

    public UsePowerMessage(String sender, Action action, int characterCard) {
        super(sender, action);
        this.characterCard = characterCard;
    }

    public int getCharacterCard(){
        return characterCard;
    }
}
