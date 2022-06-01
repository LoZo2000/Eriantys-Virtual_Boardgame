package it.polimi.ingsw;

import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.ClientGUI;

import java.io.IOException;

public class ClientApp {
    public static void main(String[] args){
        //Client client = new Client("192.168.178.56", 12346);
        //Client client = new Client("2.tcp.eu.ngrok.io", 16556);
        //ClientGUI clientGUI = new ClientGUI("192.168.178.56", 12346);
        //ClientGUI clientGUI = new ClientGUI("6.tcp.eu.ngrok.io", 11217);
        try{
            client.startClient();
            //clientGUI.run();
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }
}