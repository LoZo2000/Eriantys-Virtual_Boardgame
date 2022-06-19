package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.PingMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class create the gui for the client to play the game
 */
public class ClientGUI {
    private final String ip;
    private final int port;
    private GUIEntry GUIentry;
    private GUIGame GUIgame;

    private OutputStream outputStream;
    private final Object lockWrite;

    /**
     * This method is the constructor of the class
     * @param ip is the ip of the server in which run the model
     * @param port is the port of the application on the server
     */
    public ClientGUI(String ip, int port){
        this.ip = ip;
        this.port = port;

        this.lockWrite = new Object();
    }

    /**
     * This method is the default method called in ClientGui, it set the ping the client will exchange with the server to
     * check the connection, manage the games from the client perspective creating the window to join a game and the window
     * that contain the game
     * @throws IOException when there is an input/output error
     */
    public void run() throws IOException{
        int anothergame=0;
        while(anothergame==0){
            Socket socket = new Socket(ip, port);

            outputStream = socket.getOutputStream();

            Thread ping = new Thread(this::ping);

            ping.setDaemon(true);
            ping.start();

            GUIentry = new GUIEntry(socket, lockWrite);
            GameReport report = GUIentry.openGUI();
            GUIgame = new GUIGame(socket, report, lockWrite);
            GUIentry.hideGUI();
            anothergame=GUIgame.openGUI();
            GUIgame.hideGUI();

            ping.interrupt();
            socket.close();
        }
        System.exit(0);
    }

    private void ping(){
        boolean run = true;
        while(run){
            try{
                synchronized(lockWrite) {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(new PingMessage());
                }
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                //Game finished
                run = false;
            } catch (IOException e) {
                //Socket error
                run = false;
            }
        }
    }
}