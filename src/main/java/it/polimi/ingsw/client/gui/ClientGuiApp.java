package it.polimi.ingsw.client.gui;

import javax.swing.*;



/**
 * This is the entry point to play a GUI-based game. Its only purpose is to ask the player the address and
 * the port of the server. We suppose the player knows the address and the port of the server.
 */
public class ClientGuiApp {

    /**
     * This is the entry point to play a GUI-based game.
     */
    public static void main(String[] args){
        String ip = "";
        int port = -1;
        GUIConnection GUIconnection = new GUIConnection();

        while(port == -1){
            port = GUIconnection.getPort();
            ip = GUIconnection.getIp();
            try{
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
            }
        }



        ClientGUI clientGUI = new ClientGUI(ip, port);
        try{
            clientGUI.run();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Connection failed!","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}