package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import  it.polimi.ingsw.model.Model;

public abstract class View extends Observable<Message> implements Observer<Model> {

    protected View(){}

    protected void processChoice(Message message){
        notify(message);
    }

    protected abstract void showModel(Model model);

    @Override
    public void update(Model message) {
        showModel(message);
    }
}