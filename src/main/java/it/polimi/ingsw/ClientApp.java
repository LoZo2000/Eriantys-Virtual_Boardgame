package it.polimi.ingsw;

import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.ClientGUI;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args){
        //Client client = new Client("127.0.0.1", 12346);
        //Client client = new Client("2.tcp.eu.ngrok.io", 16556);
        ClientGUI clientGUI = new ClientGUI("127.0.0.1", 12346);
        try{
            //client.run();
            clientGUI.run();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}