package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.Game;

import java.io.*;
import java.net.Socket;

public class Connection extends Observable<Message> implements Runnable {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private PrintWriter out;
    private Server server;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Message message;
    private String owner;
    private boolean active = true;



    public Connection(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    public void send(Game game){
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(game);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void closeConnection(){
        send("Connection closed from the server side");
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    private void close(){
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void run() {
        try{
            System.out.println("Start new connection!");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            out = new PrintWriter(socket.getOutputStream());
            send("Welcome to Eriantys'world!\nWould you like to create a new game (type 'CREATEMATCH') or to join one (type 'ADDME')?");
            objectInputStream = new ObjectInputStream(inputStream);
            message = (Message) objectInputStream.readObject();
            owner = message.getSender();
            server.lobby(this, message);
            notify(message);
            while(isActive()){
                objectInputStream = new ObjectInputStream(inputStream);
                message = (Message) objectInputStream.readObject();
                notify(message);
            }
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public String toString(){
        return "Connection server-"+owner;
    }
}