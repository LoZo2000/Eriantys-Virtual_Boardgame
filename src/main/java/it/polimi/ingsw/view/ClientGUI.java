package it.polimi.ingsw.view;

import it.polimi.ingsw.messages.PingMessage;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.image.BufferedImage;
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

        System.setProperty("sun.java2d.uiScale.enabled", "false");
        System.setProperty("sun.java2d.uiScale", "1.0");

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