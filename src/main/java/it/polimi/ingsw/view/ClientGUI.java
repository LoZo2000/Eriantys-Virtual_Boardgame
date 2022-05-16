package it.polimi.ingsw.view;

import java.io.IOException;
import java.net.Socket;

public class ClientGUI {
    private String ip;
    private int port;
    private GUIEntry GUIentry;
    private GUIGame GUIgame;

    public ClientGUI(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void run() throws IOException{
        int anothergame=0;
        while(anothergame==0){
            Socket socket = new Socket(ip, port);
            GUIentry = new GUIEntry(socket);
            GameReport report = GUIentry.openGUI();
            GUIgame = new GUIGame(socket, report);
            GUIentry.hideGUI();
            anothergame=GUIgame.openGUI();
            GUIgame.hideGUI();
            socket.close();
        }
        System.exit(0);
    }
}