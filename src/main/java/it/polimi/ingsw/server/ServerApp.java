package it.polimi.ingsw.server;

import java.io.IOException;

public class ServerApp {
    public static void main( String[] args ) {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch(IOException e){
            System.err.println("Impossible to start the server!\n" + e.getMessage());
        }
    }
}