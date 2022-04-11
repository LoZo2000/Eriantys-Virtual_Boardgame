package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import  it.polimi.ingsw.model.Model;

public class RemoteView extends View {

    private class MessageReceiver implements Observer<Message> {

        @Override
        public void update(Message message) {
            System.out.println("Received instruction from: " + message.getSender());
            try{
                processChoice(message);
            } catch (IllegalArgumentException e) {
                connection.send("Error! Make your move");
            }
        }
    }

    private Connection connection;

    public RemoteView(Connection c){
        super();
        this.connection = c;
        c.addObserver(new MessageReceiver());
    }

    @Override
    protected void showModel(Model model) {
        System.out.println("Sending something through "+connection);
        connection.send(model.getGame());
    }
}