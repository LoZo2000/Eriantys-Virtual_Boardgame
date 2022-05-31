package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;

public class RulesDialog extends JFrame{
    private Dialog d;
    private JButton closeButton;

    public RulesDialog(JFrame window){
        //Background image:
        ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Credits_bg.png"));
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(390, 400,  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(390,400);

        d = new JDialog(window, "Eriantys - Rules");
        d.setSize(390, 400);
        d.setResizable(false);
        d.add(bgLabel);

        //Title of the label:
        JLabel title = new JLabel();
        title.setText("Rules:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, 22));
        title.setBounds(140,0,100,20);
        bgLabel.add(title);

        //Page buttons:
        PageDialog pageDialog = new PageDialog(window);
        for(int i=0; i<12; i++){
            JButton pbButton = new JButton();
            ImageIcon pbIcon = new ImageIcon(this.getClass().getResource("/Rules_pag"+String.valueOf(i)+".png"));
            Image pbImage = pbIcon.getImage();
            newImg = pbImage.getScaledInstance(80, 80,  Image.SCALE_SMOOTH);
            pbIcon = new ImageIcon(newImg);
            pbButton.setIcon(pbIcon);
            pbButton.setContentAreaFilled(false);
            pbButton.setBounds(10+90*(i%4), 25+100*(i/4), 80, 80);
            pbButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bgLabel.add(pbButton);
            JLabel bg = new JLabel();
            bg.setText("Page "+String.valueOf(i+1));
            bg.setFont(new Font("MV Boli", Font.BOLD, 10));
            bg.setHorizontalAlignment(SwingConstants.CENTER);
            bg.setVerticalAlignment(SwingConstants.CENTER);
            bg.setBounds(10+90*(i%4),105+100*(i/4),80,15);
            bgLabel.add(bg);
            final int page = i;
            pbButton.addActionListener(e->{
                pageDialog.showDialog(page);
            });
        }

        //Button to close dialog:
        closeButton = new JButton();
        ImageIcon closeIcon = new ImageIcon(this.getClass().getResource("/Back.png"));
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(165, 315, 40, 40);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(closeButton);
        closeButton.addActionListener(e->{
            d.setVisible(false);
        });
    }



    public void showDialog(){
        if(d.isVisible()) d.setVisible(false);
        else d.setVisible(true);
    }
}