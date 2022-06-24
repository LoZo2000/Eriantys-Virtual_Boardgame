package it.polimi.ingsw.server;

import java.io.IOException;

/**
 * This is the entry point to play a CLI-based game. Its only purpose is to ask the player the address and
 * the port of the server. We suppose the player knows the address and the port of the server.
 */
public class ServerApp {

    /**
     * This is the entry point to launch the server
     */
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