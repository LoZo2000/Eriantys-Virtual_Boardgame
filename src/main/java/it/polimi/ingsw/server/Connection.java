package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.view.GameReport;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The class connection represent the connection of every socket, it implements the observer pattern and is observed by the private
 * class MessageReceiver in the RemoteView, the role of the class connection is to manage the connection, take the messages sent by
 * the client and notify them
 */
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
    private InetAddress sockaddr;
    private boolean active = true;


    /**
     * This method is the constructor of the class
     * @param socket is the socket associated with the connection
     * @param server is the Server class associated
     */
    public Connection(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        sockaddr = socket.getInetAddress();
    }

    private synchronized boolean isActive(){
        return active;
    }

    /**
     * This method is used to send String through the connection
     * @param message is the string to send
     */
    public void send(String message){
        out.println(message);
        out.flush();
    }

    /**
     * This method is used to send the GameReport through the connection
     * @param gr is the GameReport to send
     */
    public void send(GameReport gr){
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gr);
        }catch (Exception e){
            //e.printStackTrace();
            System.out.println("Connection with user " + owner + " is already closed");
        }
    }

    /**
     * This method is used to close the socket (and the connection)
     */
    public synchronized void closeConnection(){
        //send("Connection closed from the server side");
        try{
            socket.close();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
        active = false;
    }

    private void close(){
        closeConnection();
        System.out.println("Deregistering client " + owner);
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    /**
     * This method is the principal method of the class, receive the messages and notify them to the RemoteView through
     * the message receiver
     */
    @Override
    public void run() {
        try{
            System.out.println("Start new connection!");

            this.socket.setSoTimeout(20000);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            out = new PrintWriter(socket.getOutputStream());
            do {
                objectInputStream = new ObjectInputStream(inputStream);
                message = (Message) objectInputStream.readObject();

                if(message.getAction() != Action.PING) {
                    if (server.isNotValidNickname(message.getSender())) {
                        send(new GameReport(null, "This nickname is already taken", null, false));
                    }
                } else{
                    send(new GameReport(null, "PONG", null, false));
                }
            }while (server.isNotValidNickname(message.getSender()));
            server.lobby(this, (AddMeMessage) message);
            notify(message);
            owner = message.getSender();
            server.registerNickname(owner);
            while(isActive()){
                objectInputStream = new ObjectInputStream(inputStream);
                message = (Message) objectInputStream.readObject();

                if(message.getAction() != Action.PING)
                    notify(message);
                else
                    send(new GameReport(null, "PONG", null, false));
            }
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    /**
     * This method return the nickname of the owner of the connection
     * @return a String containing the name of the owner of the connection
     */
    public String getNickname(){
        return owner;
    }

    /**
     * This method is the override of the method ToString for this class
     * @return the string to print if the method is called
     */
    public String toString(){
        return "Connection server-"+owner;
    }
}