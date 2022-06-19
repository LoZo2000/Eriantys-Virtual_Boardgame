package it.polimi.ingsw;

import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.ClientGUI;


/**
 * This class is the class invoked to start the game, create a new ClientGui or Client object depending on the use of the
 * cli or the gui and call the method run on it
 */
public class ClientApp {
    public static void main(String[] args){
        //Client client = new Client("127.0.0.1", 12346);
        //Client client = new Client("2.tcp.eu.ngrok.io", 16556);
        ClientGUI clientGUI = new ClientGUI("127.0.0.1", 12346);
        //ClientGUI clientGUI = new ClientGUI("6.tcp.eu.ngrok.io", 11217);
        try{
            //client.startClient();
            clientGUI.run();
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
}