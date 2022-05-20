package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Observer;

public class RemoteView extends View {

    private class MessageReceiver implements Observer<Message> {

        @Override
        public void update(Message message) {
            System.out.println("Received instruction from: " + message.getSender() + " " + message.getAction());
            try{
                processChoice(message);
            } catch (IllegalArgumentException e) {
                connection.send("Error! Make your move");
            }
        }
    }

    private Connection connection;
    private String owner;

    public RemoteView(Connection c, String s){
        super();
        this.connection = c;
        this.owner = s;
        c.addObserver(new MessageReceiver());
    }

    @Override
    protected void showModel(GameReport gr) {
        if(gr.getNamePlayer().equals(owner)){
            System.out.println("Sending something to the client through "+connection);
            connection.send(gr);
        }
    }
}