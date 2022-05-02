package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Update;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;

public abstract class ChooseColorMessage extends Message{
    protected final Color chosenColor;

    public ChooseColorMessage(String sender, Action action, Color color) {
        super(sender, action);
        this.chosenColor = color;
    }

    public Color getChosenColor(){
        return this.chosenColor;
    }
}
