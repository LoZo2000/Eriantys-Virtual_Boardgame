package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.model.Color;

public class ChooseColorMessage extends Message{
    private final Color chosenColor;

    public ChooseColorMessage(String sender, Action action, Color color) {
        super(sender, action);
        this.chosenColor = color;
    }

    public Color getChosenColor(){
        return this.chosenColor;
    }
}
