package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PageDialog {
    private Dialog d;
    private int currentPage = -1;
    private JPanel pageContainer = new JPanel();

    public PageDialog(JFrame window){
        d = new JDialog(window, "Eriantys - Rules");
        d.setSize(815, 725);
        d.setResizable(false);

        JPanel bg = new JPanel();
        bg.setLayout(null);
        bg.setBounds(0, 0, 795, 730);
        d.add(bg);

        //Right button:
        JButton rightButton = new JButton();
        ImageIcon rightIcon = new ImageIcon(this.getClass().getResource("/Right.png"));
        Image rightImage = rightIcon.getImage();
        Image newImg = rightImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        rightIcon = new ImageIcon(newImg);
        rightButton.setIcon(rightIcon);
        rightButton.setContentAreaFilled(false);
        rightButton.setBorderPainted(false);
        rightButton.setBounds(755, 330, 40, 40);
        rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(rightButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = new ImageIcon(this.getClass().getResource("/Halo_green.png"));
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(755, 330, 40, 40);
        halo.setVisible(false);
        bg.add(halo);

        rightButton.addActionListener(e->{
            pageContainer.removeAll();
            currentPage = (currentPage+1)%12;
            ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Rules_pag"+ String.valueOf(currentPage)+".png"));
            Image bgImage = bgIcon.getImage();
            Image newImg2 = bgImage.getScaledInstance(700, 700,  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg2);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(700,700);
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
        ImageIcon leftIcon = new ImageIcon(this.getClass().getResource("/Left.png"));
        Image leftImage = leftIcon.getImage();
        newImg = leftImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        leftIcon = new ImageIcon(newImg);
        leftButton.setIcon(leftIcon);
        leftButton.setContentAreaFilled(false);
        leftButton.setBorderPainted(false);
        leftButton.setBounds(5, 330, 40, 40);
        leftButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bg.add(leftButton);

        JLabel halo2 = new JLabel();
        ImageIcon halo2Icon = new ImageIcon(this.getClass().getResource("/Halo_green.png"));
        Image halo2Image = halo2Icon.getImage();
        newImg =halo2Image.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        halo2Icon = new ImageIcon(newImg);
        halo2.setIcon(halo2Icon);
        halo2.setOpaque(false);
        halo2.setBounds(5, 330, 40, 40);
        halo2.setVisible(false);
        bg.add(halo2);

        leftButton.addActionListener(e->{
            pageContainer.removeAll();
            currentPage = (currentPage+11)%12;
            ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Rules_pag"+ String.valueOf(currentPage)+".png"));
            Image bgImage = bgIcon.getImage();
            Image newImg2 = bgImage.getScaledInstance(700, 700,  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg2);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(700,700);
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

        pageContainer.setBounds(50, 0, 700, 700);
        bg.add(pageContainer);
    }



    public void showDialog(int page){
        if(d.isVisible() && page==currentPage){
            d.setVisible(false);
            currentPage = -1;
        }
        else{
            pageContainer.removeAll();
            ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Rules_pag"+ String.valueOf(page)+".png"));
            Image bgImage = bgIcon.getImage();
            Image newImg = bgImage.getScaledInstance(700, 700,  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(700,700);
            pageContainer.add(bgLabel);
            pageContainer.repaint();
            currentPage = page;
            d.setVisible(true);
        }
    }
}