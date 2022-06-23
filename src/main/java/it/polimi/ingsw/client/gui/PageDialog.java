package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.gui.ScalingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represent the dialog containing the board of the other player
 */
public class PageDialog {
    private final Dialog d;
    private int currentPage = -1;
    private final JPanel pageContainer = new JPanel();

    /**
     * This method is the constructor of the class
     * @param window is the main window of the game
     */
    public PageDialog(JFrame window){
        d = new JDialog(window, "Eriantys - Rules");
        d.setSize(ScalingUtils.scaleX(815), ScalingUtils.scaleY(725));
        d.setResizable(false);

        JPanel bg = new JPanel();
        bg.setLayout(null);
        bg.setBounds(0, 0, ScalingUtils.scaleX(795, 815), ScalingUtils.scaleY(730, 725));
        d.add(bg);

        //Right button:
        JButton rightButton = new JButton();
        ImageIcon rightIcon = ScalingUtils.getImage("/Right.png");
        Image rightImage = rightIcon.getImage();
        Image newImg = rightImage.getScaledInstance(ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730),  Image.SCALE_SMOOTH);
        rightIcon = new ImageIcon(newImg);
        rightButton.setIcon(rightIcon);
        rightButton.setContentAreaFilled(false);
        rightButton.setBorderPainted(false);
        rightButton.setBounds(ScalingUtils.scaleX(755, 795), ScalingUtils.scaleY(330, 730), ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730));
        rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(rightButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_green.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(755, 795), ScalingUtils.scaleY(330, 730), ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730));
        halo.setVisible(false);
        bg.add(halo);

        rightButton.addActionListener(e->{
            pageContainer.removeAll();
            currentPage = (currentPage+1)%12;
            ImageIcon bgIcon = ScalingUtils.getImage("/Rules_pag" + currentPage + ".png");
            Image bgImage = bgIcon.getImage();
            Image newImg2 = bgImage.getScaledInstance(ScalingUtils.scaleX(700, 700), ScalingUtils.scaleY(700, 700),  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg2);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(ScalingUtils.scaleX(700, 700),ScalingUtils.scaleY(700, 700));
            pageContainer.add(bgLabel);
            pageContainer.repaint();
        });
        rightButton.addMouseListener(new MouseAdapter() {
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

        //Left button:
        JButton leftButton = new JButton();
        ImageIcon leftIcon = ScalingUtils.getImage("/Left.png");
        Image leftImage = leftIcon.getImage();
        newImg = leftImage.getScaledInstance(ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730), Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(newImg);
        leftButton.setIcon(leftIcon);
        leftButton.setContentAreaFilled(false);
        leftButton.setBorderPainted(false);
        leftButton.setBounds(ScalingUtils.scaleX(5, 795), ScalingUtils.scaleY(330, 730), ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730));
        leftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(leftButton);

        JLabel halo2 = new JLabel();
        ImageIcon halo2Icon = ScalingUtils.getImage("/Halo_green.png");
        Image halo2Image = halo2Icon.getImage();
        newImg =halo2Image.getScaledInstance(ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730),  Image.SCALE_SMOOTH);
        halo2Icon = new ImageIcon(newImg);
        halo2.setIcon(halo2Icon);
        halo2.setOpaque(false);
        halo2.setBounds(ScalingUtils.scaleX(5, 795), ScalingUtils.scaleY(330, 730), ScalingUtils.scaleX(40, 795), ScalingUtils.scaleY(40, 730));
        halo2.setVisible(false);
        bg.add(halo2);

        leftButton.addActionListener(e->{
            pageContainer.removeAll();
            currentPage = (currentPage+11)%12;
            ImageIcon bgIcon = ScalingUtils.getImage("/Rules_pag" + currentPage +".png");
            Image bgImage = bgIcon.getImage();
            Image newImg2 = bgImage.getScaledInstance(ScalingUtils.scaleX(700, 700), ScalingUtils.scaleY(700, 700),  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg2);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(ScalingUtils.scaleX(700, 700),ScalingUtils.scaleY(700, 700));
            pageContainer.add(bgLabel);
            pageContainer.repaint();
        });
        leftButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                halo2.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseEntered(e);
                halo2.setVisible(false);
            }
        });

        pageContainer.setBounds(ScalingUtils.scaleX(50, 795), 0, ScalingUtils.scaleX(700, 795), ScalingUtils.scaleY(700, 725));
        bg.add(pageContainer);
    }


    /**
     * This method show the page dialog
     * @param page represent the page to show
     */
    public void showDialog(int page){
        if(d.isVisible() && page==currentPage){
            d.setVisible(false);
            currentPage = -1;
        }
        else{
            pageContainer.removeAll();
            ImageIcon bgIcon = ScalingUtils.getImage("/Rules_pag" + page +".png");
            Image bgImage = bgIcon.getImage();
            Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(700, 700), ScalingUtils.scaleY(700, 700),  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(ScalingUtils.scaleX(700, 700), ScalingUtils.scaleY(700, 700));
            pageContainer.add(bgLabel);
            pageContainer.repaint();
            currentPage = page;
            d.setVisible(true);
        }
    }
}