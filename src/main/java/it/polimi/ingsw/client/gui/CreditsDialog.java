package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.ScalingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class represent the dialog containing the credits of the game
 */
public class CreditsDialog extends JFrame{
    private final Dialog d;

    /**
     * This method is the constructor of the class
     * @param window is the main window
     */
    public CreditsDialog(JFrame window){
        //Background image:
        ImageIcon bgIcon = ScalingUtils.getImage("/Credits_bg.png");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(380, 380), ScalingUtils.scaleY(355, 355),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(380, 380),ScalingUtils.scaleX(355, 355));

        d = new JDialog(window, "Eriantys - Credits");
        d.setSize(ScalingUtils.scaleX(380), ScalingUtils.scaleX(355));
        d.setResizable(false);
        d.add(bgLabel);

        //Title of the label:
        JLabel title = new JLabel();
        title.setText("Credits:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(22)));
        title.setBounds(ScalingUtils.scaleX(140, 380),0,ScalingUtils.scaleX(100, 380),ScalingUtils.scaleY(20, 355));
        bgLabel.add(title);

        //All credits:
        JLabel cre = new JLabel();
        cre.setFont(new Font("Dialog", Font.BOLD, ScalingUtils.scaleFont(11)));
        cre.setText("<html>Game design: Leo Colovini<br/>Development: Simone Luciani<br/>Art: Alessandro Costa Kapakkione<br/>Graphics: Elisabetta Micucci<br/>Rules: David Digby & Stefania Niccolini<br/>Edition: Giuliano Acquati & Lorenzo Tucci Sorrentino</html>");
        cre.setHorizontalTextPosition(SwingConstants.CENTER);
        cre.setVerticalTextPosition(SwingConstants.CENTER);
        cre.setHorizontalAlignment(SwingConstants.CENTER);
        cre.setVerticalAlignment(SwingConstants.CENTER);
        cre.setBounds(ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(20, 355), ScalingUtils.scaleX(300, 380), ScalingUtils.scaleY(100, 355));
        bgLabel.add(cre);

        //Rights:
        JLabel rig = new JLabel();
        rig.setText("©2021 Cranio Creations Ltd: All rights reserved.");
        rig.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(15)));
        rig.setHorizontalTextPosition(SwingConstants.CENTER);
        rig.setVerticalTextPosition(SwingConstants.CENTER);
        rig.setHorizontalAlignment(SwingConstants.CENTER);
        rig.setVerticalAlignment(SwingConstants.CENTER);
        rig.setBounds(ScalingUtils.scaleX(40, 380),ScalingUtils.scaleY(120, 355), ScalingUtils.scaleX(300, 380), ScalingUtils.scaleY(20, 355));
        bgLabel.add(rig);

        //CranioCreations logo:
        ImageIcon logoIcon = ScalingUtils.getImage("/CranioCreations_logo.png");
        Image logoImage = logoIcon.getImage();
        newImg = logoImage.getScaledInstance(ScalingUtils.scaleX(80, 380), ScalingUtils.scaleY(80, 355),  Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(newImg);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(ScalingUtils.scaleX(150, 380),ScalingUtils.scaleY(140, 355),ScalingUtils.scaleX(80, 380), ScalingUtils.scaleY(80, 355));
        bgLabel.add(logoLabel);

        //Our credits:
        JLabel pro = new JLabel();
        pro.setText("<html>Developers (GC11):<br/>Spagnolo Gabriele, Vallone Mario & Zoccatelli Lorenzo</html>");
        pro.setFont(new Font("Dialog", Font.BOLD, ScalingUtils.scaleFont(11)));
        pro.setHorizontalTextPosition(SwingConstants.CENTER);
        pro.setVerticalTextPosition(SwingConstants.CENTER);
        pro.setHorizontalAlignment(SwingConstants.CENTER);
        pro.setVerticalAlignment(SwingConstants.CENTER);
        pro.setBounds(ScalingUtils.scaleX(15, 380), ScalingUtils.scaleY(220, 355), ScalingUtils.scaleX(350, 380),ScalingUtils.scaleY(30, 355));
        bgLabel.add(pro);

        //Attribute the website FlatIcon:
        JLabel att = new JLabel();
        att.setText("Sprites from www.flaticon.com");
        att.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(15)));
        att.setHorizontalTextPosition(SwingConstants.CENTER);
        att.setVerticalTextPosition(SwingConstants.CENTER);
        att.setHorizontalAlignment(SwingConstants.CENTER);
        att.setVerticalAlignment(SwingConstants.CENTER);
        att.setBounds(ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(250, 355), ScalingUtils.scaleX(300, 380),ScalingUtils.scaleY(20, 355));
        bgLabel.add(att);

        //Button to close dialog:
        JButton closeButton = new JButton();
        ImageIcon closeIcon = ScalingUtils.getImage("/Back.png");
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(40, 355),  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(ScalingUtils.scaleX(170, 380), ScalingUtils.scaleY(270, 355), ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(40, 355));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(closeButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_purple.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(40, 355),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(170, 380), ScalingUtils.scaleY(270, 355), ScalingUtils.scaleX(40, 380), ScalingUtils.scaleY(40, 355));
        halo.setVisible(false);
        bgLabel.add(halo);

        closeButton.addActionListener(e->{
            d.setVisible(false);
        });
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                halo.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                halo.setVisible(false);
            }
        });
    }

    /**
     * This method set the dialog visible
     */
    public void showDialog(){
        d.setVisible(!d.isVisible());
    }
}