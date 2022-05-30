package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.view.GameReport;


import java.io.*;
import java.net.InetAddress;
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
    private InetAddress sockaddr;
    private boolean active = true;



    public Connection(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        sockaddr = socket.getInetAddress();
    }

    private synchronized boolean isActive(){
        return active;
    }

    public void send(String message){
        out.println(message);
        out.flush();
    }

    public void send(GameReport gr){
        try {
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gr);
        }catch (Exception e){
            //e.printStackTrace();
            System.out.println("Connection with user " + owner + " is already closed");
        }
    }

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
            }
        } catch(Exception e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    public String getNickname(){
        return owner;
    }

    public String toString(){
        return "Connection server-"+owner;
    }
}