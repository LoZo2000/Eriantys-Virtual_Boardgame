package it.polimi.ingsw.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represent the dialog containing the rules of the game
 */
public class RulesDialog extends JFrame{
    private final Dialog d;

    /**
     * This method is the constructor of the class
     * @param window is the main window of the game
     */
    public RulesDialog(JFrame window){
        //Background image:
        ImageIcon bgIcon = ScalingUtils.getImage("/Credits_bg.png");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(390), ScalingUtils.scaleY(400),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(390),ScalingUtils.scaleY(400));

        d = new JDialog(window, "Eriantys - Rules");
        d.setSize(ScalingUtils.scaleX(390), ScalingUtils.scaleY(400));
        d.setResizable(false);
        d.add(bgLabel);

        //Title of the label:
        JLabel title = new JLabel();
        title.setText("Rules:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(22)));
        title.setBounds(ScalingUtils.scaleX(140, 390), 0, ScalingUtils.scaleX(100, 390), ScalingUtils.scaleY(20, 355));
        bgLabel.add(title);

        //Page buttons:
        PageDialog pageDialog = new PageDialog(window);
        for(int i=0; i<12; i++){
            JButton pbButton = new JButton();
            ImageIcon pbIcon = ScalingUtils.getImage("/Rules_pag"+i+".png");
            Image pbImage = pbIcon.getImage();
            newImg = pbImage.getScaledInstance(ScalingUtils.scaleX(80, 390), ScalingUtils.scaleY(80, 400),  Image.SCALE_SMOOTH);
            pbIcon = new ImageIcon(newImg);
            pbButton.setIcon(pbIcon);
            pbButton.setContentAreaFilled(false);
            pbButton.setBounds(ScalingUtils.scaleX(10+90*(i%4),390), ScalingUtils.scaleY(25+100*(i/4), 400), ScalingUtils.scaleX(80, 390), ScalingUtils.scaleY(80, 400));
            pbButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bgLabel.add(pbButton);
            JLabel bg = new JLabel();
            bg.setText("Page "+(i+1));
            bg.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(10)));
            bg.setHorizontalAlignment(SwingConstants.CENTER);
            bg.setVerticalAlignment(SwingConstants.CENTER);
            bg.setBounds(ScalingUtils.scaleX(10+90*(i%4), 390),ScalingUtils.scaleY(105+100*(i/4), 400), ScalingUtils.scaleX(80, 390), ScalingUtils.scaleY(15, 400));
            bgLabel.add(bg);
            final int page = i;
            pbButton.addActionListener(e->{
                pageDialog.showDialog(page);
            });
        }

        //Button to close dialog:
        JButton closeButton = new JButton();
        ImageIcon closeIcon = ScalingUtils.getImage("/Back.png");
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(ScalingUtils.scaleX(40, 390), ScalingUtils.scaleY(40, 400),  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(ScalingUtils.scaleX(165, 390), ScalingUtils.scaleY(315, 400), ScalingUtils.scaleX(40, 390), ScalingUtils.scaleY(40, 400));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(closeButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_purple.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 390), ScalingUtils.scaleY(40, 400),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(165, 390), ScalingUtils.scaleY(315, 400), ScalingUtils.scaleX(40, 390), ScalingUtils.scaleY(40, 400));
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