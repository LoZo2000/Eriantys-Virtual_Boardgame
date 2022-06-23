package it.polimi.ingsw.client.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;



/**
 * This class builds the Java Swing window to let the player insert the server's address and port
 */
public class GUIConnection {
    private String ip;
    private int port = -1;

    /**
     * This is the creator of the class
     */
    public GUIConnection(){
        //Background image:
        ImageIcon bgIcon = ScalingUtils.getImage("/Game_bg.jpg");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(495, 495), ScalingUtils.scaleY(255, 255),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(495, 495), ScalingUtils.scaleY(255, 255));

        //Main window:
        JFrame window = new JFrame("Eriantys - Connection settings");
        window.add(bgLabel);
        window.setSize(ScalingUtils.scaleX(495), ScalingUtils.scaleY(255));
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //Local panel:
        JLabel lp = new JLabel();
        lp.setLayout(null);
        lp.setOpaque(true);
        Color colorLabel = new Color(190, 190, 190,200);
        lp.setBackground(colorLabel);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        lp.setBorder(border);
        lp.setBounds(ScalingUtils.scaleX(8, 495), ScalingUtils.scaleY(8, 255), ScalingUtils.scaleX(458, 495), ScalingUtils.scaleY(53, 255));
        bgLabel.add(lp);

        //Local text:
        JLabel localText = new JLabel();
        localText.setText("<html>Click here if the server<br/>is running on this pc</html>");
        localText.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(10)));
        localText.setHorizontalAlignment(SwingConstants.CENTER);
        localText.setVerticalAlignment(SwingConstants.CENTER);
        localText.setBounds(ScalingUtils.scaleX(8, 495), ScalingUtils.scaleY(8, 255), ScalingUtils.scaleX(180, 495), ScalingUtils.scaleY(38, 255));
        lp.add(localText);

        //Local button:
        JButton localButton = new JButton("Connect to local server");
        localButton.setBounds(ScalingUtils.scaleX(188, 495), ScalingUtils.scaleY(8, 255), ScalingUtils.scaleX(263, 495), ScalingUtils.scaleY(38, 255));
        localButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        localButton.setFocusPainted(false);
        lp.add(localButton);
        localButton.addActionListener(e->{
            synchronized (this) {
                ip = "127.0.0.1";
                port = 12346;
                notifyAll();
            }
            window.setVisible(false);
        });

        //Online panel:
        JLabel op = new JLabel();
        op.setLayout(null);
        op.setOpaque(true);
        op.setBackground(colorLabel);
        op.setBorder(border);
        op.setBounds(ScalingUtils.scaleX(8, 495), ScalingUtils.scaleY(68, 255), ScalingUtils.scaleX(458, 495), ScalingUtils.scaleY(120, 255));
        bgLabel.add(op);

        //Ip text:
        JLabel ipLabel = new JLabel();
        ipLabel.setText("Server's ip:");
        ipLabel.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(12)));
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setVerticalAlignment(SwingConstants.CENTER);
        ipLabel.setBounds(ScalingUtils.scaleX(8, 495), ScalingUtils.scaleY(8, 255), ScalingUtils.scaleX(150, 495), ScalingUtils.scaleY(30, 255));
        op.add(ipLabel);

        //TextField to get server's ip
        JTextField ipField = new JTextField(16);
        ipField.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        ipField.setBounds(ScalingUtils.scaleX(165, 495), ScalingUtils.scaleY(8, 255), ScalingUtils.scaleX(278, 495), ScalingUtils.scaleY(30, 255));
        op.add(ipField);

        //Port text:
        JLabel portLabel = new JLabel();
        portLabel.setText("Server's port:");
        portLabel.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(12)));
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setVerticalAlignment(SwingConstants.CENTER);
        portLabel.setBounds(ScalingUtils.scaleX(8, 495), ScalingUtils.scaleY(45, 255), ScalingUtils.scaleX(150, 495), ScalingUtils.scaleY(30, 255));
        op.add(portLabel);

        //TextField to get server's port
        JTextField portField = new JTextField(16);
        portField.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        portField.setBounds(ScalingUtils.scaleX(165, 495), ScalingUtils.scaleY(45, 255), ScalingUtils.scaleX(278, 495), ScalingUtils.scaleY(30, 255));
        op.add(portField);

        //Online button:
        JButton onlineButton = new JButton("Connect to online server");
        onlineButton.setBounds(ScalingUtils.scaleX(98, 495), ScalingUtils.scaleY(83, 255), ScalingUtils.scaleX(263, 495), ScalingUtils.scaleY(30, 255));
        onlineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        onlineButton.setFocusPainted(false);
        op.add(onlineButton);
        onlineButton.addActionListener(e->{
            try{
                synchronized (this){
                    port = Integer.parseInt(portField.getText());
                    ip = ipField.getText();
                    notifyAll();
                }
                window.setVisible(false);
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null, "The port must be an integer!","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        window.setVisible(true);
    }


    /**
     * Class to get the attribute ip
     * @return the ip of the server the player wants to connect to
     */
    public String getIp(){
        return ip;
    }

    /**
     * Class to get the attribute port
     * @return the port of the server the player wants to connect to
     */
    public int getPort(){
        return port;
    }
}