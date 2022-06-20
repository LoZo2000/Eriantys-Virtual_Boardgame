package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.Connection;
import it.polimi.ingsw.server.Observer;

/**
 * This class represent the view in the server, it inherits the class view
 */
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

    /**
     * This method is the constructor of the class
     * @param c is the connection of the player owning the view
     * @param s is the nickname of hte player owning the view
     */
    public RemoteView(Connection c, String s){
        super();
        this.connection = c;
        this.owner = s;
        c.addObserver(new MessageReceiver());
    }

    /**
     * This method is called to send the state of the game to the view of the player when the model modify its state and
     * notify the view
     * @param gr is the GameReport representing the state of the game (model)
     */
    @Override
    protected void showModel(GameReport gr) {
        if(gr.getNamePlayer().equals(owner)){
            System.out.println("Sending something to the client through "+connection);
            connection.send(gr);
        }
    }
}