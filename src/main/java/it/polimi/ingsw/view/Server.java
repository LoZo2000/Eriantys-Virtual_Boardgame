package it.polimi.ingsw.view;

import  it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Action;
import  it.polimi.ingsw.model.Model;

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

    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<Connection> connections = new ArrayList<>();

    //Other:
    Model model;
    Controller controller;

    //Match's features:
    private boolean completeRules = false;
    private int maxPlayers = 0;
    private int currentPlayers = 1;



    //Register connection
    private synchronized void registerConnection(Connection c){
        connections.add(c);
    }

    //Deregister connection
    public synchronized void deregisterConnection(Connection c){
        connections.remove(c);
        c.closeConnection();
    }

    public synchronized void lobby(Connection c, Message message){
        if(message.getAction()==Action.CREATEMATCH && maxPlayers==0){
            completeRules = message.getCompleteRules();
            maxPlayers = message.getNumPlayers();
            model = new Model();
            controller = new Controller(model);
            RemoteView remoteView = new RemoteView(c);
            remoteView.addObserver(controller);
            model.addObserver(remoteView);
        }
        else if(message.getAction()==Action.ADDME && currentPlayers<maxPlayers){
            currentPlayers++;
            RemoteView remoteView = new RemoteView(c);
            remoteView.addObserver(controller);
            model.addObserver(remoteView);
        }
        else{
            c.closeConnection();
        }
    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
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
}