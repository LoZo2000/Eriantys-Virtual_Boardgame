package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.messages.PingMessage;
import it.polimi.ingsw.view.GameReport;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class create the gui for the client to play the game
 */
public class ClientGUI {
    private String ip;
    private int port;
    private GUIEntry GUIentry;
    private GUIGame GUIgame;

    private ObjectOutputStream objectOutputStream;
    private final Object lockWrite;



    /**
     * This method is the constructor of the class
     * @param ip is the ip of the server in which run the model
     * @param port is the port of the application on the server
     */
    public ClientGUI(String ip, int port){
        this.ip = ip;
        this.port = port;

        UIManager.put("OptionPane.minimumSize",new Dimension(ScalingUtils.scaleX(262), ScalingUtils.scaleY(85)));

        FontUIResource old = (FontUIResource) UIManager.getFont("OptionPane.font");
        FontUIResource newFont = new FontUIResource(old.getFamily(), Font.BOLD, ScalingUtils.scaleFont(old.getSize()));
        UIManager.put("OptionPane.messageFont", newFont);

        FontUIResource newFontButton = new FontUIResource(old.getFamily(), old.getStyle(), ScalingUtils.scaleFont(old.getSize()));
        UIManager.put("OptionPane.buttonFont", newFontButton);

        Icon iconError = UIManager.getIcon("OptionPane.errorIcon");
        UIManager.put("OptionPane.errorIcon", ScalingUtils.scaleDefaultIcon(iconError));

        Icon iconWarning = UIManager.getIcon("OptionPane.warningIcon");
        UIManager.put("OptionPane.warningIcon", ScalingUtils.scaleDefaultIcon(iconWarning));

        Icon iconQuestion = UIManager.getIcon("OptionPane.questionIcon");
        UIManager.put("OptionPane.questionIcon", ScalingUtils.scaleDefaultIcon(iconQuestion));

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

            socket.setSoTimeout(20000);

            ObjectInputStream  objectInputStream= new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            Thread ping = new Thread(this::ping);

            ping.setDaemon(true);
            ping.start();

            GUIentry = new GUIEntry(socket, lockWrite, objectInputStream, objectOutputStream);
            GameReport report = GUIentry.openGUI();
            GUIgame = new GUIGame(socket, report, lockWrite, objectInputStream, objectOutputStream);
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
                    //ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.reset();
                    objectOutputStream.writeObject(new PingMessage());
                    objectOutputStream.flush();
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