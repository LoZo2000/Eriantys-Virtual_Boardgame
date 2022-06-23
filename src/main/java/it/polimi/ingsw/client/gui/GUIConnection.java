package it.polimi.ingsw.client.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;



/**
 * This class builds the Java Swing window to let the player to insert the server's address and port
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
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(330), ScalingUtils.scaleY(170),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(330), ScalingUtils.scaleY(170));

        JLayeredPane layered = new JLayeredPane();
        layered.add(bgLabel, Integer.valueOf(5));
        layered.setSize(ScalingUtils.scaleX(910), ScalingUtils.scaleY(705));
        layered.setLayout(null);

        //Main window:
        JFrame window = new JFrame("Eriantys - Connection settings");
        window.add(layered);
        window.setSize(ScalingUtils.scaleX(330), ScalingUtils.scaleY(170));
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
        lp.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(5), ScalingUtils.scaleX(305), ScalingUtils.scaleY(35));
        bgLabel.add(lp);

        //Local text:
        JLabel localText = new JLabel();
        localText.setText("<html>Click here if the server<br/>is running on this pc</html>");
        localText.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(10)));
        localText.setHorizontalAlignment(SwingConstants.CENTER);
        localText.setVerticalAlignment(SwingConstants.CENTER);
        localText.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(5), ScalingUtils.scaleX(120), ScalingUtils.scaleY(25));
        lp.add(localText);

        //Local button:
        JButton localButton = new JButton("Connect to local server");
        localButton.setBounds(ScalingUtils.scaleX(125), ScalingUtils.scaleY(5), ScalingUtils.scaleX(175), ScalingUtils.scaleY(25));
        localButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lp.add(localButton);
        localButton.addActionListener(e->{
            ip = "127.0.0.1";
            port = 12346;
            window.setVisible(false);
        });

        //Online panel:
        JLabel op = new JLabel();
        op.setLayout(null);
        op.setOpaque(true);
        op.setBackground(colorLabel);
        op.setBorder(border);
        op.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(45), ScalingUtils.scaleX(305), ScalingUtils.scaleY(80));
        bgLabel.add(op);

        //Ip text:
        JLabel ipLabel = new JLabel();
        ipLabel.setText("Server's ip:");
        ipLabel.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(12)));
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setVerticalAlignment(SwingConstants.CENTER);
        ipLabel.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(5), ScalingUtils.scaleX(100), ScalingUtils.scaleY(20));
        op.add(ipLabel);

        //TextField to get server's ip
        JTextField ipField = new JTextField(16);
        ipField.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        ipField.setBounds(ScalingUtils.scaleX(110), ScalingUtils.scaleY(5), ScalingUtils.scaleX(185), ScalingUtils.scaleY(20));
        op.add(ipField);

        //Port text:
        JLabel portLabel = new JLabel();
        portLabel.setText("Server's port:");
        portLabel.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(12)));
        portLabel.setHorizontalAlignment(SwingConstants.CENTER);
        portLabel.setVerticalAlignment(SwingConstants.CENTER);
        portLabel.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(30), ScalingUtils.scaleX(100), ScalingUtils.scaleY(20));
        op.add(portLabel);

        //TextField to get server's port
        JTextField portField = new JTextField(16);
        portField.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        portField.setBounds(ScalingUtils.scaleX(110), ScalingUtils.scaleY(30), ScalingUtils.scaleX(185), ScalingUtils.scaleY(20));
        op.add(portField);

        //Online button:
        JButton onlineButton = new JButton("Connect to online server");
        onlineButton.setBounds(ScalingUtils.scaleX(65), ScalingUtils.scaleY(55), ScalingUtils.scaleX(175), ScalingUtils.scaleY(20));
        onlineButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        op.add(onlineButton);
        onlineButton.addActionListener(e->{
            try{
                port = Integer.parseInt(portField.getText());
                ip = ipField.getText();
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