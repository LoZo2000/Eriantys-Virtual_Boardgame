package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;

public class PageDialog {
    private Dialog d;

    public PageDialog(JFrame window){
        d = new JDialog(window, "Eriantys - Rules");
        d.setSize(900, 900);
        d.setResizable(false);
    }



    public void showDialog(int page){
        if(d.isVisible()) d.setVisible(false);
        else{
            d.removeAll();
            ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Rules_pag"+ String.valueOf(page)+".png"));
            Image bgImage = bgIcon.getImage();
            Image newImg = bgImage.getScaledInstance(900, 900,  Image.SCALE_SMOOTH);
            bgIcon = new ImageIcon(newImg);
            JLabel bgLabel = new JLabel(bgIcon);
            bgLabel.setSize(900,900);
            d.add(bgLabel);
            d.repaint();
            d.setVisible(true);
        }
    }
}