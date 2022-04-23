package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;

public class ChooseIslandMessage extends Message{
    private final int idIsland;

    public ChooseIslandMessage(String sender, Action action, int idIsland) {
        super(sender, action);
        this.idIsland = idIsland;
    }

    public int getIdIsland(){
        return this.idIsland;
    }
}
