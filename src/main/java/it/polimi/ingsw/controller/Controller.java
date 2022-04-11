package it.polimi.ingsw.controller;

import it.polimi.ingsw.messages.Message;
import  it.polimi.ingsw.model.Model;
import  it.polimi.ingsw.view.Observer;

public class Controller implements Observer<Message> {

    private Model model;

    public Controller(Model model){
        this.model = model;
    }

    @Override
    public void update(Message message) {
        model.setPlayerChoice(message);
    }
}