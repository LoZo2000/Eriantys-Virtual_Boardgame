package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The class Server represents the server of the application, this class manage new socket connection using the class gameManager and
 * Connection
 */
public class Server {
    private static final int PORT = 12346;
    private ServerSocket serverSocket;
    private GameManager gameMaker;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<Connection> connections = new ArrayList<>();
    private List<String> registeredNicknames;

    //Register connection
    private synchronized void registerConnection(Connection c){
        connections.add(c);
    }

    /**
     * This method is used to deregister a connection, it removes the connections from the list of active connections and
     * notify other players in the game that the connection with one of the other player has been closed
     * @param c is the Connection to deregister
     */
    //Deregister connection
    public synchronized void deregisterConnection(Connection c){
        connections.remove(c);
        this.registeredNicknames.remove(c.getNickname());
        Controller controller = gameMaker.searchGameByNickname(c.getNickname());
        if(controller != null)
            controller.disconnectedPlayer(c.getNickname());
        //else
            //System.out.println("ALREADY REMOVED GAME");
        c.closeConnection();
    }

    /**
     * This method is the constructor of the class
     * @throws IOException is an exception that can be thrown by the ServerSocket constructor
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.gameMaker = new GameManager();

        this.registeredNicknames = new ArrayList<>();
    }

    /**
     * This method is called to add a player to a game according to his preferences
     * @param c is the connection of the player
     * @param message is the AddMeMessage sent by the player
     */
    public synchronized void lobby(Connection c, AddMeMessage message){
        gameMaker.joinLobby(c, message);
    }

    /**
     * This method is the principal method of the server and continuously accepts new connections and manage them
     */
    public void run(){
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this);
                registerConnection(connection);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    /**
     * This method register the nickname of the players in the games
     * @param nickname is a String representing the nickname to register
     */
    public synchronized void registerNickname(String nickname){
        this.registeredNicknames.add(nickname);
    }

    /**
     * This method check if the nickname is already used by another player in the server, so if it is valid
     * @param nickname is a String representing the nickname to check
     * @return a boolean to report if the nickname is available or not
     */
    public synchronized boolean isNotValidNickname(String nickname){
        return this.registeredNicknames.contains(nickname) || nickname == null;
    }
}