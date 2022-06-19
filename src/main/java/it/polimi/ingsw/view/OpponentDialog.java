package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ColorTower;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represent the dialog containing the board of the other player
 */
public class OpponentDialog extends JFrame{
    private Dialog d;
    private JButton closeButton;
    private String opponent;

    private JPanel containerDash = new JPanel();
    private JPanel containerCard = new JPanel();
    private JLabel numC;


    /**
     * This method is the constructor of the class
     * @param window is the main window of the game
     * @param opponent is the nickname of the opponent that owns the board to show
     */
    public OpponentDialog(JFrame window, String opponent){
        this.opponent = opponent;

        ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Game_bg.jpg"));
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(610, 225,  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(610,225);

        d = new JDialog(window, "Eriantys - " + opponent +"'s dashboard");
        d.setSize(620, 260);
        d.setResizable(false);
        d.add(bgLabel);

        //Dashboard:
        ImageIcon dashIcon = new ImageIcon(this.getClass().getResource("/Dashboard.png"));
        Image dashImage = dashIcon.getImage();
        newImg = dashImage.getScaledInstance(500, 200,  Image.SCALE_SMOOTH);
        dashIcon = new ImageIcon(newImg);
        JLabel dashLabel = new JLabel(dashIcon);
        dashLabel.setBounds(5, 15, 500, 200);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        dashLabel.setBorder(border);
        bgLabel.add(dashLabel);

        containerDash.setBounds(0, 0, 500, 200);
        containerDash.setLayout(null);
        containerDash.setOpaque(false);
        dashLabel.add(containerDash);

        //Coins:
        JPanel coins = new JPanel();
        coins.setLayout(null);
        coins.setBounds(510, 5, 91, 20);
        Color colorPanel = new Color(128,128,128,125);
        coins.setBackground(colorPanel);
        coins.setBorder(border);
        bgLabel.add(coins);
        JLabel titleCo = new JLabel();
        titleCo.setText("Coins:");
        titleCo.setHorizontalAlignment(SwingConstants.CENTER);
        titleCo.setVerticalAlignment(SwingConstants.CENTER);
        titleCo.setFont(new Font("MV Boli", Font.BOLD, 9));
        titleCo.setBounds(0,0,50,20);
        coins.add(titleCo);
        numC = new JLabel();
        numC.setText("x0");
        numC.setHorizontalAlignment(SwingConstants.CENTER);
        numC.setVerticalAlignment(SwingConstants.CENTER);
        numC.setFont(new Font("MV Boli", Font.BOLD, 9));
        numC.setBounds(50,0,31,20);
        coins.add(numC);

        //Last card:
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(510, 30, 91, 145);
        card.setBackground(colorPanel);
        card.setBorder(border);
        bgLabel.add(card);
        JLabel titleCa = new JLabel();
        titleCa.setText("Last played card:");
        titleCa.setHorizontalAlignment(SwingConstants.CENTER);
        titleCa.setVerticalAlignment(SwingConstants.CENTER);
        titleCa.setFont(new Font("MV Boli", Font.BOLD, 9));
        titleCa.setBounds(0,0,91,20);
        card.add(titleCa);
        containerCard.setLayout(null);
        containerCard.setBounds(3, 17, 85, 125);
        card.add(containerCard);
        JLabel c = new JLabel();
        ImageIcon cIcon = new ImageIcon(this.getClass().getResource("/Char_back.png"));
        Image cImage = cIcon.getImage();
        newImg = cImage.getScaledInstance(85, 125,  Image.SCALE_SMOOTH);
        cIcon = new ImageIcon(newImg);
        c.setIcon(cIcon);
        c.setBounds(0, 0, 85, 125);
        containerCard.add(c);

        //Button to close dialog:
        closeButton = new JButton();
        ImageIcon closeIcon = new ImageIcon(this.getClass().getResource("/Back.png"));
        Image closeImage = closeIcon.getImage();
        newImg = closeImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        closeIcon = new ImageIcon(newImg);
        closeButton.setIcon(closeIcon);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setBounds(530, 180, 40, 40);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(closeButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = new ImageIcon(this.getClass().getResource("/Halo_purple.png"));
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(530, 180, 40, 40);
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


    /**
     * This method update the dialog with the refreshed values
     * @param report represent the state of the game
     * @param position represent the position of the player //todo gab controlla
     */
    public void updateDialog(GameReport report, int position){
        //Show entrance:
        containerDash.removeAll();
        int len = report.getOpponentsEntrance().get(position).size();
        for(int i=0; i<len; i++){
            JLabel st = new JLabel();
            ImageIcon stIcon = new ImageIcon(this.getClass().getResource(report.getOpponentsEntrance().get(position).get(i).getSprite()));
            Image stImage = stIcon.getImage();
            Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            stIcon = new ImageIcon(newImg);
            st.setIcon(stIcon);
            st.setOpaque(false);
            st.setBounds(10+30*((i+1)%2),20+31*((i+1)/2),  30, 30);
            containerDash.add(st);
        }
        for(int i=0; i<5; i++){
            len = report.getOpponentsCanteen().get(position).get(i);
            for(int j=0; j<len; j++){
                JLabel st = new JLabel();
                ImageIcon stIcon;
                switch (i){
                    case 0 -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_green.png"));
                    case 1 -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_red.png"));
                    case 2 -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow.png"));
                    case 3 -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_pink.png"));
                    default -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_blue.png"));
                }
                Image stImage = stIcon.getImage();
                Image newImg = stImage.getScaledInstance(23, 23,  Image.SCALE_SMOOTH);
                stIcon = new ImageIcon(newImg);
                st.setIcon(stIcon);
                st.setOpaque(false);
                st.setBounds(95+24*j,22+32*i,  23, 23);
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
            ImageIcon toIcon = new ImageIcon(this.getClass().getResource(file));
            Image toImage = toIcon.getImage();
            Image newImg = toImage.getScaledInstance(35, 45,  Image.SCALE_SMOOTH);
            toIcon = new ImageIcon(newImg);
            to.setIcon(toIcon);
            to.setOpaque(false);
            if(report.getNumPlayers()==3) to.setBounds(405+35*(i%2),85-30*(i/2),  35, 45);
            else to.setBounds(405+35*(i%2),115-30*(i/2),  35, 45);
            containerDash.add(to);
        }
        it.polimi.ingsw.model.Color[] colInDashC = {it.polimi.ingsw.model.Color.GREEN, it.polimi.ingsw.model.Color.RED, it.polimi.ingsw.model.Color.YELLOW, it.polimi.ingsw.model.Color.PINK, it.polimi.ingsw.model.Color.BLUE};
        String[] colInDashS = {"/Prof_green.png", "/Prof_red.png", "/Prof_yellow.png", "/Prof_pink.png", "/Prof_blue.png"};
        for(int i=0; i<5; i++){
            if(report.getProfessors().get(colInDashC[i]).equals(report.getOpponentsNick().get(position))){
                JLabel pro = new JLabel();
                ImageIcon proIcon = new ImageIcon(this.getClass().getResource(colInDashS[i]));
                Image proImage = proIcon.getImage();
                Image newImg = proImage.getScaledInstance(23, 23,  Image.SCALE_SMOOTH);
                proIcon = new ImageIcon(newImg);
                pro.setIcon(proIcon);
                pro.setOpaque(false);
                pro.setBounds(352,22+32*i,  23, 23);
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
            ImageIcon cIcon = new ImageIcon(this.getClass().getResource(report.getOpponentsLastCard().get(position).getFront()));
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(85, 125,  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            c.setIcon(cIcon);
            c.setBounds(0, 0, 85, 125);
            containerCard.add(c);
            containerCard.repaint();
        }
    }
}