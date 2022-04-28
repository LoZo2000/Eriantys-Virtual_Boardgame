package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.server.Observer;

public class Controller implements Observer<Message> {

    private Model model;

    public Controller(Model model){
        this.model = model;
    }

    @Override
    public void update(Message message) {
        try {
            model.setPlayerChoice(message);
        }catch(Exception e){
            model.sendErrorNote(message.getSender(), e.toString(), model.getGame().getCurrentPlayer());
        }
    }
}