package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.PingMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientGUI {
    private final String ip;
    private final int port;
    private GUIEntry GUIentry;
    private GUIGame GUIgame;

    private OutputStream outputStream;
    private final Object lockWrite;

    public ClientGUI(String ip, int port){
        this.ip = ip;
        this.port = port;

        this.lockWrite = new Object();
    }

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