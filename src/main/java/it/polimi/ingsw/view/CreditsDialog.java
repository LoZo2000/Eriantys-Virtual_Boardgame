package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CreditsDialog extends JFrame implements ActionListener {
    private boolean isVisible = false;
    private Dialog d;
    private JButton closeButton;

    public CreditsDialog(JFrame window){
        //Background image:
        ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Credits_bg.png"));
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(380, 355,  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(380,355);

        d = new JDialog(window, "Eriantys - Credits");
        d.setSize(380, 355);
        d.setResizable(false);
        d.add(bgLabel);

        //Title of the label:
        JLabel title = new JLabel();
        title.setText("Credits:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, 22));
        title.setBounds(140,0,100,20);
        bgLabel.add(title);

        //All credits:
        JLabel cre = new JLabel();
        cre.setText("<html>Game design: Leo Colovini<br/>Development: Simone Luciani<br/>Art: Alessandro Costa Kapakkione<br/>Graphics: Elisabetta Micucci<br/>Rules: David Digby & Stefania Niccolini<br/>Edition: Giuliano Acquati & Lorenzo Tucci Sorrentino</html>");
        cre.setHorizontalTextPosition(SwingConstants.CENTER);
        cre.setVerticalTextPosition(SwingConstants.CENTER);
        cre.setHorizontalAlignment(SwingConstants.CENTER);
        cre.setVerticalAlignment(SwingConstants.CENTER);
        cre.setBounds(40,20,300,100);
        bgLabel.add(cre);

        //Rights:
        JLabel rig = new JLabel();
        rig.setText("©2021 Cranio Creations Ltd: All rights reserved.");
        rig.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        rig.setHorizontalTextPosition(SwingConstants.CENTER);
        rig.setVerticalTextPosition(SwingConstants.CENTER);
        rig.setHorizontalAlignment(SwingConstants.CENTER);
        rig.setVerticalAlignment(SwingConstants.CENTER);
        rig.setBounds(40,120,300,20);
        bgLabel.add(rig);

        //CranioCreations logo:
        ImageIcon logoIcon = new ImageIcon(this.getClass().getResource("/CranioCreations_logo.png"));
        Image logoImage = logoIcon.getImage();
        newImg = logoImage.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(newImg);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(150,140,80,80);
        bgLabel.add(logoLabel);

        //Our credits:
        JLabel pro = new JLabel();
        pro.setText("<html>Developers (GC11):<br/>Spagnolo Gabriele, Vallone Mario & Zoccatelli Lorenzo</html>");
        pro.setHorizontalTextPosition(SwingConstants.CENTER);
        pro.setVerticalTextPosition(SwingConstants.CENTER);
        pro.setHorizontalAlignment(SwingConstants.CENTER);
        pro.setVerticalAlignment(SwingConstants.CENTER);
        pro.setBounds(15,220,350,30);
        bgLabel.add(pro);

        //Attribute the website FlatIcon:
        JLabel att = new JLabel();
        att.setText("Sprites from www.flaticon.com");
        att.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        att.setHorizontalTextPosition(SwingConstants.CENTER);
        att.setVerticalTextPosition(SwingConstants.CENTER);
        att.setHorizontalAlignment(SwingConstants.CENTER);
        att.setVerticalAlignment(SwingConstants.CENTER);
        att.setBounds(40,250,300,20);
        bgLabel.add(att);

        //Button to close dialog:
        closeButton = new JButton();
        closeButton.addActionListener(this);
        ImageIcon closeIcon = new ImageIcon(this.getClass().getResource("/Back.png"));
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBounds(170, 270, 40, 40);
        bgLabel.add(closeButton);
    }



    public void showDialog(){
        if(d.isVisible()) d.setVisible(false);
        else d.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == closeButton){
            d.setVisible(false);
        }
    }
}