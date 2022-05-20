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



public class Server {
    private static final int PORT = 12346;
    private ServerSocket serverSocket;
    private GameManager gameMaker;

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<Connection> connections = new ArrayList<>();
    private List<String> registeredNicknames;


    /*public synchronized void deliverMessage(Connection c, Message message) throws IllegalActionException {
        gameMaker.manageMessage(c, message);
    }*/

    //Register connection
    private synchronized void registerConnection(Connection c){
        connections.add(c);
    }

    //Deregister connection
    public synchronized void deregisterConnection(Connection c){
        connections.remove(c);
        this.registeredNicknames.remove(c.getNickname());
        Controller controller = gameMaker.searchGameByNickname(c.getNickname());
        if(controller != null)
            controller.disconnectedPlayer(c.getNickname());
        else
            //TODO DEBUG MESSAGE
            System.out.println("ALREADY REMOVED GAME");
        c.closeConnection();
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.gameMaker = new GameManager();

        this.registeredNicknames = new ArrayList<>();
    }

    //To sort the players among matches availables according to their preferences
    public synchronized void lobby(Connection c, AddMeMessage message){
        gameMaker.joinLobby(c, message);
    }

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

    public synchronized void registerNickname(String nickname){
        this.registeredNicknames.add(nickname);
    }

    public synchronized boolean isNotValidNickname(String nickname){
        return this.registeredNicknames.contains(nickname);
    }
}