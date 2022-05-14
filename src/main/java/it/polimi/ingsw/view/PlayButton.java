package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class PlayButton {
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private boolean requestSent = false;
    private String nickname;


    public PlayButton(JLabel label){
        ImageIcon playIcon = new ImageIcon(this.getClass().getResource("/Play.png"));
        Image playImage = playIcon.getImage();
        Image newImg = playImage.getScaledInstance(50, 50,  Image.SCALE_SMOOTH);
        playIcon = new ImageIcon(newImg);
        JButton playButton = new JButton();
        playButton.setIcon(playIcon);
        playButton.setOpaque(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setFocusPainted(false);
        playButton.setBounds(220, 90, 60, 60);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        label.add(playButton);
    }
}