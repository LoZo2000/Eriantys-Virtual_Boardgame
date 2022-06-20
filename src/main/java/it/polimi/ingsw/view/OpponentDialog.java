package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ColorTower;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represent the dialog containing the board of the other player
 */
public class OpponentDialog extends JFrame{
    private final Dialog d;
    private final JPanel containerDash = new JPanel();
    private final JPanel containerCard = new JPanel();
    private final JLabel numC;
    private JButton closeButton;
    private String opponent;


    /**
     * This method is the constructor of the class
     * @param window is the main window of the game
     * @param opponent is the nickname of the opponent that owns the board to show
     */
    public OpponentDialog(JFrame window, String opponent){

        ImageIcon bgIcon = ScalingUtils.getImage("/Game_bg.jpg");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(610, 620), ScalingUtils.scaleY(225, 260),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(610, 620), ScalingUtils.scaleY(225, 260));

        d = new JDialog(window, "Eriantys - " + opponent +"'s dashboard");
        d.setSize(ScalingUtils.scaleX(620), ScalingUtils.scaleY(260));
        d.setResizable(false);
        d.add(bgLabel);

        //Dashboard:
        ImageIcon dashIcon = ScalingUtils.getImage("/Dashboard.png");
        Image dashImage = dashIcon.getImage();
        newImg = dashImage.getScaledInstance(ScalingUtils.scaleX(500, 620), ScalingUtils.scaleY(200, 260),  Image.SCALE_SMOOTH);
        dashIcon = new ImageIcon(newImg);
        JLabel dashLabel = new JLabel(dashIcon);
        dashLabel.setBounds(ScalingUtils.scaleX(5, 620), ScalingUtils.scaleY(15, 260), ScalingUtils.scaleX(500, 620), ScalingUtils.scaleY(200, 260));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        dashLabel.setBorder(border);
        bgLabel.add(dashLabel);

        containerDash.setBounds(0, 0, ScalingUtils.scaleX(500, 620), ScalingUtils.scaleY(200, 260));
        containerDash.setLayout(null);
        containerDash.setOpaque(false);
        dashLabel.add(containerDash);

        //Coins:
        JPanel coins = new JPanel();
        coins.setLayout(null);
        coins.setBounds(ScalingUtils.scaleX(510, 620), ScalingUtils.scaleY(5, 260), ScalingUtils.scaleX(91, 620), ScalingUtils.scaleY(20, 260));
        Color colorPanel = new Color(128,128,128,125);
        coins.setBackground(colorPanel);
        coins.setBorder(border);
        bgLabel.add(coins);
        JLabel titleCo = new JLabel();
        titleCo.setText("Coins:");
        titleCo.setHorizontalAlignment(SwingConstants.CENTER);
        titleCo.setVerticalAlignment(SwingConstants.CENTER);
        titleCo.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        titleCo.setBounds(0,0, ScalingUtils.scaleX(50, 91), ScalingUtils.scaleY(20, 20));
        coins.add(titleCo);
        numC = new JLabel();
        numC.setText("x0");
        numC.setHorizontalAlignment(SwingConstants.CENTER);
        numC.setVerticalAlignment(SwingConstants.CENTER);
        numC.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        numC.setBounds(ScalingUtils.scaleX(50, 91),0, ScalingUtils.scaleX(31, 91), ScalingUtils.scaleY(20, 20));
        coins.add(numC);

        //Last card:
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(ScalingUtils.scaleX(510, 620), ScalingUtils.scaleY(30, 260), ScalingUtils.scaleX(91, 620), ScalingUtils.scaleY(145, 260));
        card.setBackground(colorPanel);
        card.setBorder(border);
        bgLabel.add(card);
        JLabel titleCa = new JLabel();
        titleCa.setText("Last played card:");
        titleCa.setHorizontalAlignment(SwingConstants.CENTER);
        titleCa.setVerticalAlignment(SwingConstants.CENTER);
        titleCa.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        titleCa.setBounds(0,0, ScalingUtils.scaleX(91, 91), ScalingUtils.scaleY(20, 145));
        card.add(titleCa);
        containerCard.setLayout(null);
        containerCard.setBounds(ScalingUtils.scaleX(3, 91), ScalingUtils.scaleY(17, 145), ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
        card.add(containerCard);
        JLabel c = new JLabel();
        ImageIcon cIcon = ScalingUtils.getImage("/Char_back.png");
        Image cImage = cIcon.getImage();
        newImg = cImage.getScaledInstance(ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145),  Image.SCALE_SMOOTH);
        cIcon = new ImageIcon(newImg);
        c.setIcon(cIcon);
        c.setBounds(0, 0, ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
        containerCard.add(c);

        //Button to close dialog:
        JButton closeButton = new JButton();
        ImageIcon closeIcon = ScalingUtils.getImage("/Back.png");
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(ScalingUtils.scaleX(40, 620), ScalingUtils.scaleY(40, 260),  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(ScalingUtils.scaleX(530, 620), ScalingUtils.scaleY(180, 260), ScalingUtils.scaleX(40, 620), ScalingUtils.scaleY(40, 260));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(closeButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_purple.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 620), ScalingUtils.scaleY(40, 260),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(530, 620), ScalingUtils.scaleY(180, 260), ScalingUtils.scaleX(40, 620), ScalingUtils.scaleY(40, 260));
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
        if(d.isVisible()) d.setVisible(false);
        else d.setVisible(true);
    }

    //TODO Mario controlla
    /**
     * This method update the dialog with the refreshed values
     * @param report represent the state of the game
     * @param position represent the position of the opponent in the report list.
     */
    public void updateDialog(GameReport report, int position){
        //Show entrance:
        containerDash.removeAll();
        int len = report.getOpponentsEntrance().get(position).size();
        for(int i=0; i<len; i++){
            JLabel st = new JLabel();
            ImageIcon stIcon = ScalingUtils.getImage(report.getOpponentsEntrance().get(position).get(i).getSprite());
            Image stImage = stIcon.getImage();
            Image newImg = stImage.getScaledInstance(ScalingUtils.scaleX(30, 500), ScalingUtils.scaleY(30, 200),  Image.SCALE_SMOOTH);
            stIcon = new ImageIcon(newImg);
            st.setIcon(stIcon);
            st.setOpaque(false);
            st.setBounds(ScalingUtils.scaleX(10+30*((i+1)%2), 500), ScalingUtils.scaleY(20+31*((i+1)/2), 200),  ScalingUtils.scaleX(30, 500), ScalingUtils.scaleY(30, 200));
            containerDash.add(st);
        }
        for(int i=0; i<5; i++){
            len = report.getOpponentsCanteen().get(position).get(i);
            for(int j=0; j<len; j++){
                JLabel st = new JLabel();
                ImageIcon stIcon;
                switch (i){
                    case 0 -> stIcon = ScalingUtils.getImage("/Stud_green.png");
                    case 1 -> stIcon = ScalingUtils.getImage("/Stud_red.png");
                    case 2 -> stIcon = ScalingUtils.getImage("/Stud_yellow.png");
                    case 3 -> stIcon = ScalingUtils.getImage("/Stud_pink.png");
                    default -> stIcon = ScalingUtils.getImage("/Stud_blue.png");
                }
                Image stImage = stIcon.getImage();
                Image newImg = stImage.getScaledInstance(ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200),  Image.SCALE_SMOOTH);
                stIcon = new ImageIcon(newImg);
                st.setIcon(stIcon);
                st.setOpaque(false);
                st.setBounds(ScalingUtils.scaleX(95+24*j, 500), ScalingUtils.scaleY(22+32*i, 200), ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200));
                containerDash.add(st);
            }
        }
        len = report.getTowersInDash().get(position);
        ColorTower myCT = report.getAllPlayersColor().get(position);
        String file;
        switch (myCT){
            case BLACK -> file = "/Tower_black.png";
            case WHITE -> file = "/Tower_white.png";
            default -> file = "/Tower_grey.png";
        }
        for(int i=0; i<len; i++){
            JLabel to = new JLabel();
            ImageIcon toIcon = ScalingUtils.getImage(file);
            Image toImage = toIcon.getImage();
            Image newImg = toImage.getScaledInstance(ScalingUtils.scaleX(35, 500), ScalingUtils.scaleX(45, 200),  Image.SCALE_SMOOTH);
            toIcon = new ImageIcon(newImg);
            to.setIcon(toIcon);
            to.setOpaque(false);
            if(report.getNumPlayers()==3) to.setBounds(ScalingUtils.scaleX(405+35*(i%2), 500), ScalingUtils.scaleY(85-30*(i/2), 200), ScalingUtils.scaleX(35, 500), ScalingUtils.scaleX(45, 200));
            else to.setBounds(ScalingUtils.scaleX(405+35*(i%2), 500), ScalingUtils.scaleY(115-30*(i/2), 200), ScalingUtils.scaleX(35, 500), ScalingUtils.scaleX(45, 200));
            containerDash.add(to);
        }
        it.polimi.ingsw.model.Color[] colInDashC = {it.polimi.ingsw.model.Color.GREEN, it.polimi.ingsw.model.Color.RED, it.polimi.ingsw.model.Color.YELLOW, it.polimi.ingsw.model.Color.PINK, it.polimi.ingsw.model.Color.BLUE};
        String[] colInDashS = {"/Prof_green.png", "/Prof_red.png", "/Prof_yellow.png", "/Prof_pink.png", "/Prof_blue.png"};
        for(int i=0; i<5; i++){
            if(report.getProfessors().get(colInDashC[i]).equals(report.getOpponentsNick().get(position))){
                JLabel pro = new JLabel();
                ImageIcon proIcon = ScalingUtils.getImage(colInDashS[i]);
                Image proImage = proIcon.getImage();
                Image newImg = proImage.getScaledInstance(ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200),  Image.SCALE_SMOOTH);
                proIcon = new ImageIcon(newImg);
                pro.setIcon(proIcon);
                pro.setOpaque(false);
                pro.setBounds(ScalingUtils.scaleX(352, 500), ScalingUtils.scaleX(22+32*i, 200),  ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200));
                containerDash.add(pro);
            }
        }
        containerDash.repaint();

        //Show coins:
        numC.setText("x"+report.getOpponentsCoins().get(position));

        //Show played card:
        if(report.getOpponentsLastCard().get(position) != null){
            containerCard.removeAll();
            JLabel c = new JLabel();
            ImageIcon cIcon = ScalingUtils.getImage(report.getOpponentsLastCard().get(position).getFront());
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145),  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            c.setIcon(cIcon);
            c.setBounds(0, 0, ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
            containerCard.add(c);
            containerCard.repaint();
        }
    }
}