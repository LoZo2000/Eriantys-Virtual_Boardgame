package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.characters.ActionCharacter;
import it.polimi.ingsw.model.characters.CharacterType;
import it.polimi.ingsw.model.characters.MovementCharacter;
import it.polimi.ingsw.view.GameReport;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class is the object that represent the window that contain the game to play
 */
public class GUIGame {

    private static class TransparentPanel extends JPanel {
        public TransparentPanel(){
            setOpaque(false);
        }

        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            Rectangle r = g.getClipBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            super.paintComponent(g);
        }
    }

    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;
    private final JFrame window = new JFrame("Eriantys"); //Main window
    private final JLayeredPane layered = new JLayeredPane();
    private JLabel bgLabel; //Main background
    private String myNick;
    private OpponentDialog[] OD = new OpponentDialog[4];
    private ArrayList<JLabel> halos;

    //Container to empty and refill every time we receive a GameReport
    private final JPanel containerIslands = new JPanel();
    private final JPanel containerInfo = new JPanel();
    private final JPanel containerDash = new JPanel();
    private final JPanel containerCard = new JPanel();
    private final JPanel containerZoomedCard = new JPanel();
    private final JPanel containerLastCard = new JPanel();
    private final ArrayList<JPanel> containerCharacters = new ArrayList<>();
    private Phase currentPhase;
    private JLabel numC; //To display coins available

    //Variables to move a student:
    private int idStudentToMove = -1;
    private it.polimi.ingsw.model.Color colorStudentToMove;
    private Location departureType;
    private int departureID;

    //Variable to move MotherNature:
    private int posMT;

    private final Object lockWrite;
    //Characters'buttons:
    private final ArrayList<JButton> characterButtons = new ArrayList<>();

    private final JPanel loading = new TransparentPanel();

    /**
     * This method is the constructor of the class
     * @param socket is the socket of the connection with the server
     * @param report is the report of the game returned by the GUIEntry
     * @param lockWrite is a lock to synchronize the object
     * @throws IOException
     */
    public GUIGame(Socket socket, GameReport report, Object lockWrite, final ObjectInputStream in, final ObjectOutputStream out) throws IOException {
        objectInputStream = in;
        objectOutputStream = out;
        this.lockWrite = lockWrite;

        createGUI(report);
        displayReport(report);
    }


    /**
     * This method is called to open the gui to play the game
     * @return an int representing if the player want to play another game or not (0 if the player want to play another game, 1 otherwise)
     */
    public int openGUI(){
        window.setVisible(true);
        while(true){
            try {
                //objectInputStream = new ObjectInputStream(inputStream);
                GameReport report = (GameReport) objectInputStream.readObject();
                if(report.getError()==null) {
                    startLoading();
                    displayReport(report);
                }
                else if(!report.getError().equals("PONG")){
                    JOptionPane.showMessageDialog(null, report.getError(),"Eriantys - Illegal move", JOptionPane.WARNING_MESSAGE);
                    idStudentToMove = -1;
                    turnOffAllHalos();

                    layered.setLayer(loading, 1);
                    layered.repaint();
                }
                if(report.getFinishedGame()){
                    layered.setLayer(loading, 1);
                    layered.repaint();

                    int confirm;
                    if(report.getWinner()==null){ confirm=JOptionPane.showConfirmDialog(null, "The game is finished."+" \nDo you want to play another game?", "Eriantys" ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null);}
                    else{confirm=JOptionPane.showConfirmDialog(null, "The game is finished, the winner is: "+report.getWinner()+". \nDo you want to play another game?", "Eriantys" ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null);}
                    return confirm;
                }
            }catch (SocketTimeoutException e){
                JOptionPane.showMessageDialog(null, "<html>Server unreachable, the game is finished.<br>Check your connection and retry","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                return 1;
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                return 1;
            }
        }
    }

    private void createGUI(GameReport report){
        myNick = report.getNamePlayer();

        //Background image:
        ImageIcon bgIcon = ScalingUtils.getImage("/Game_bg.jpg");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(910), ScalingUtils.scaleY(670),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(910), ScalingUtils.scaleY(670));

        layered.add(bgLabel, Integer.valueOf(5));
        layered.setSize(ScalingUtils.scaleX(910), ScalingUtils.scaleY(705));
        layered.setLayout(null);

        //Main window:
        window.add(layered);
        window.setSize(ScalingUtils.scaleX(910), ScalingUtils.scaleY(705));
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //Help screen
        JPanel helpScreen = new JPanel();
        helpScreen.setLayout(null);
        helpScreen.setOpaque(false);
        helpScreen.setBounds(0,0, ScalingUtils.scaleX(900), ScalingUtils.scaleY(705));
        helpScreen.setVisible(false);
        buildHelpScreen(helpScreen, report.getNumPlayers());
        layered.add(helpScreen, Integer.valueOf(7));

        //My cards'panel:
        JPanel myCards = new JPanel();
        myCards.setLayout(null);
        myCards.setBounds(ScalingUtils.scaleX(-2), ScalingUtils.scaleY(631), ScalingUtils.scaleX(900), ScalingUtils.scaleY(50));
        Color colorLabel = new Color(128,128,128,255);
        myCards.setBackground(colorLabel);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        myCards.setBorder(border);
        bgLabel.add(myCards);

        JLabel titleC = new JLabel();
        titleC.setText("Your cards:");
        titleC.setHorizontalAlignment(SwingConstants.CENTER);
        titleC.setVerticalAlignment(SwingConstants.CENTER);
        titleC.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(13)));
        titleC.setBounds(ScalingUtils.scaleX(5, 900), ScalingUtils.scaleY(10, 45), ScalingUtils.scaleX(75, 900), ScalingUtils.scaleY(20, 45));
        myCards.add(titleC);

        containerCard.setBounds(ScalingUtils.scaleX(85, 900), ScalingUtils.scaleY(2, 45), ScalingUtils.scaleX(900, 900), ScalingUtils.scaleY(45, 45));
        containerCard.setLayout(null);
        containerCard.setOpaque(false);
        myCards.add(containerCard);

        containerZoomedCard.setBounds(ScalingUtils.scaleX(70), ScalingUtils.scaleY(480), ScalingUtils.scaleX(900), ScalingUtils.scaleY(155));
        containerZoomedCard.setLayout(null);
        containerZoomedCard.setOpaque(false);
        containerZoomedCard.setVisible(false);
        bgLabel.add(containerZoomedCard);

        //Show help Buttons:
        JButton ruleButton = new JButton();
        ImageIcon ruleIcon = ScalingUtils.getImage("/Book.png");
        Image ruleImage = ruleIcon.getImage();
        newImg = ruleImage.getScaledInstance(ScalingUtils.scaleX(35), ScalingUtils.scaleY(35),  Image.SCALE_SMOOTH);
        ruleIcon = new ImageIcon(newImg);
        ruleButton.setIcon(ruleIcon);
        ruleButton.setContentAreaFilled(false);
        ruleButton.setBorderPainted(false);
        ruleButton.setBounds(ScalingUtils.scaleX(10), ScalingUtils.scaleY(430),  ScalingUtils.scaleX(35), ScalingUtils.scaleY(35));
        ruleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(ruleButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_red.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(43), ScalingUtils.scaleY(43),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(6), ScalingUtils.scaleY(426), ScalingUtils.scaleX(43), ScalingUtils.scaleY(43));
        halo.setVisible(false);
        bgLabel.add(halo);

        RulesDialog rulesDialog = new RulesDialog(window);
        ruleButton.addActionListener(e->{
            rulesDialog.showDialog();
        });
        ruleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                halo.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e){
                super.mouseExited(e);
                halo.setVisible(false);
            }
        });

        JButton tutorButton = new JButton();
        ImageIcon tutorIcon = ScalingUtils.getImage("/Help.png");
        Image tutorImage = tutorIcon.getImage();
        newImg = tutorImage.getScaledInstance(ScalingUtils.scaleX(35), ScalingUtils.scaleY(35),  Image.SCALE_SMOOTH);
        tutorIcon = new ImageIcon(newImg);
        tutorButton.setIcon(tutorIcon);
        tutorButton.setContentAreaFilled(false);
        tutorButton.setBorderPainted(false);
        tutorButton.setBounds(ScalingUtils.scaleX(10), ScalingUtils.scaleY(470),  ScalingUtils.scaleX(35), ScalingUtils.scaleY(35));
        tutorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(tutorButton);

        JLabel halo2 = new JLabel();
        ImageIcon halo2Icon = ScalingUtils.getImage("/Halo_black.png");
        Image halo2Image = halo2Icon.getImage();
        newImg =halo2Image.getScaledInstance(ScalingUtils.scaleX(45), ScalingUtils.scaleY(45),  Image.SCALE_SMOOTH);
        halo2Icon = new ImageIcon(newImg);
        halo2.setIcon(halo2Icon);
        halo2.setOpaque(false);
        halo2.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(465), ScalingUtils.scaleX(45), ScalingUtils.scaleY(45));
        halo2.setVisible(false);
        bgLabel.add(halo2);

        tutorButton.addActionListener(e->{
            if(helpScreen.isVisible()) helpScreen.setVisible(false);
            else helpScreen.setVisible(true);
        });
        tutorButton.addMouseListener(new MouseAdapter() {
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

        //Show my dashboards:
        ImageIcon dashIcon = ScalingUtils.getImage("/Dashboard.png");
        Image dashImage = dashIcon.getImage();
        newImg = dashImage.getScaledInstance(ScalingUtils.scaleX(500), ScalingUtils.scaleY(200),  Image.SCALE_SMOOTH);
        dashIcon = new ImageIcon(newImg);
        JLabel dashLabel = new JLabel(dashIcon);
        dashLabel.setBounds(ScalingUtils.scaleX(55), ScalingUtils.scaleY(430), ScalingUtils.scaleX(500), ScalingUtils.scaleY(200));
        dashLabel.setBorder(border);
        bgLabel.add(dashLabel);

        containerDash.setBounds(0, 0, ScalingUtils.scaleX(500, 500), ScalingUtils.scaleY(200, 200));
        containerDash.setLayout(null);
        containerDash.setOpaque(false);
        dashLabel.add(containerDash);

        //Coins:
        JPanel coins = new JPanel();
        coins.setLayout(null);
        coins.setBounds(ScalingUtils.scaleX(565), ScalingUtils.scaleY(430), ScalingUtils.scaleX(91), ScalingUtils.scaleY(20));
        Color colorPanel = new Color(128,128,128,255);
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
        card.setBounds(ScalingUtils.scaleX(565), ScalingUtils.scaleY(460), ScalingUtils.scaleX(91), ScalingUtils.scaleY(145));
        card.setBackground(colorLabel);
        card.setBorder(border);
        bgLabel.add(card);
        JLabel titleCa = new JLabel();
        titleCa.setText("Last played card:");
        titleCa.setHorizontalAlignment(SwingConstants.CENTER);
        titleCa.setVerticalAlignment(SwingConstants.CENTER);
        titleCa.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        titleCa.setBounds(0,0, ScalingUtils.scaleX(91, 91), ScalingUtils.scaleY(20, 145));
        card.add(titleCa);
        containerLastCard.setLayout(null);
        containerLastCard.setBounds(ScalingUtils.scaleX(3, 91), ScalingUtils.scaleY(17, 145), ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
        card.add(containerLastCard);
        JLabel c = new JLabel();
        ImageIcon cIcon = ScalingUtils.getImage("/Char_back.png");
        Image cImage = cIcon.getImage();
        newImg = cImage.getScaledInstance(ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145),  Image.SCALE_SMOOTH);
        cIcon = new ImageIcon(newImg);
        c.setIcon(cIcon);
        c.setBounds(0, 0, ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
        containerLastCard.add(c);

        //Opponents'container:
        JPanel opponents = new JPanel();
        opponents.setLayout(null);
        opponents.setBounds(ScalingUtils.scaleX(710), ScalingUtils.scaleY(55), ScalingUtils.scaleX(180), ScalingUtils.scaleY(24+report.getNumPlayers()*22));
        opponents.setBackground(colorLabel);
        opponents.setBorder(border);
        bgLabel.add(opponents);

        JLabel titleO = new JLabel();
        titleO.setText("All players:");
        titleO.setHorizontalAlignment(SwingConstants.CENTER);
        titleO.setVerticalAlignment(SwingConstants.CENTER);
        titleO.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(13)));
        titleO.setBounds(ScalingUtils.scaleX(2, 180),0, ScalingUtils.scaleX(170, 180), ScalingUtils.scaleY(20, 24+report.getNumPlayers()*22));
        opponents.add(titleO);

        for(int i=0; i<report.getNumPlayers(); i++){
            JButton od = new JButton(report.getOpponentsNick().get(i));
            switch (report.getAllPlayersColor().get(i)){
                case WHITE -> od.setBackground(new Color(255, 255, 255, 255));
                case BLACK -> {
                    od.setBackground(new Color(0, 0, 0, 255));
                    od.setForeground(new Color(255, 255, 255, 255));
                }
                case GREY -> od.setBackground(new Color(130, 130, 130, 255));
            }
            if(report.getOpponentsNick().get(i).equals(report.getNamePlayer())) od.setEnabled(false);
            else{
                final int f = i;
                OD[i] = new OpponentDialog(window, report.getOpponentsNick().get(i));
                od.addActionListener(e->{
                    OD[f].showDialog();
                });
            }
            od.setBounds(ScalingUtils.scaleX(2, 180), ScalingUtils.scaleY(22+22*i, 24+report.getNumPlayers()*22), ScalingUtils.scaleX(170, 180), ScalingUtils.scaleY(20, 24+report.getNumPlayers()*22));
            opponents.add(od);
            od.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        //Other infos container:
        JPanel info = new JPanel();
        info.setLayout(null);
        info.setBounds(ScalingUtils.scaleX(710), ScalingUtils.scaleY(10), ScalingUtils.scaleX(180), ScalingUtils.scaleY(40));
        info.setBackground(colorLabel);
        info.setBorder(border);
        bgLabel.add(info);

        JLabel infoO = new JLabel();
        infoO.setText("Current player:");
        infoO.setHorizontalAlignment(SwingConstants.CENTER);
        infoO.setVerticalAlignment(SwingConstants.CENTER);
        infoO.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        infoO.setBounds(ScalingUtils.scaleX(2, 180),0, ScalingUtils.scaleX(70, 180), ScalingUtils.scaleY(20, 40));
        info.add(infoO);

        JLabel info1 = new JLabel();
        info1.setText("Current phase:");
        info1.setHorizontalAlignment(SwingConstants.CENTER);
        info1.setVerticalAlignment(SwingConstants.CENTER);
        info1.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        info1.setBounds(ScalingUtils.scaleX(2, 180), ScalingUtils.scaleY(20, 40), ScalingUtils.scaleX(70, 180), ScalingUtils.scaleY(20, 40));
        info.add(info1);

        containerInfo.setBounds(ScalingUtils.scaleX(72, 180), 0, ScalingUtils.scaleX(105, 180), ScalingUtils.scaleY(60, 40));
        containerInfo.setLayout(null);
        containerInfo.setOpaque(false);
        info.add(containerInfo);

        containerIslands.setBounds(ScalingUtils.scaleX(5), ScalingUtils.scaleY(5), ScalingUtils.scaleX(700), ScalingUtils.scaleY(420));
        containerIslands.setLayout(null);
        containerIslands.setOpaque(false);
        bgLabel.add(containerIslands);

        //Loading Panel
        loading.setLayout(null);
        loading.setBackground(new Color(210, 210, 210, 150));
        loading.setSize(ScalingUtils.scaleX(910), ScalingUtils.scaleY(670));
        JLabel wait = new JLabel();
        wait.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(20)));
        wait.setBounds(0, 0, ScalingUtils.scaleX(910, 910), ScalingUtils.scaleY(670, 670));
        wait.setText("Waiting server response...");
        wait.setVerticalAlignment(SwingConstants.CENTER);
        wait.setHorizontalAlignment(SwingConstants.CENTER);
        loading.add(wait);
        layered.add(loading, Integer.valueOf(1));

        //Show characters'cards if complete rules:
        for(int i=0; i<report.getChar().size(); i++){
            ImageIcon charIcon = ScalingUtils.getImage(report.getChar().get(i).getSprite());
            Image charImage = charIcon.getImage();
            newImg = charImage.getScaledInstance(ScalingUtils.scaleX(105), ScalingUtils.scaleY(145),  Image.SCALE_SMOOTH);
            charIcon = new ImageIcon(newImg);
            JLabel charLabel = new JLabel(charIcon);
            charLabel.setBounds(ScalingUtils.scaleX(770), ScalingUtils.scaleY(150*i+175), ScalingUtils.scaleX(105), ScalingUtils.scaleY(145));
            charLabel.setBorder(border);
            bgLabel.add(charLabel);

            JButton cb = new JButton();
            cb.setContentAreaFilled(false);
            cb.setBounds(0, 0, ScalingUtils.scaleX(105, 105), ScalingUtils.scaleY(145, 145));
            charLabel.add(cb);
            characterButtons.add(cb);

            final int numThisCard = i;
            cb.addActionListener(e-> {
                try{
                    synchronized (lockWrite) {
                        //objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.reset();
                        objectOutputStream.writeObject(new UsePowerMessage(myNick, numThisCard));
                        objectOutputStream.flush();
                        startLoading();
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cb.setToolTipText(report.getChar().get(i).getDesc_short());

            containerCharacters.add(new JPanel());
            containerCharacters.get(i).setBounds(0, 0, ScalingUtils.scaleX(105, 105), ScalingUtils.scaleY(145, 145));
            containerCharacters.get(i).setLayout(null);
            containerCharacters.get(i).setOpaque(false);
            charLabel.add(containerCharacters.get(i));
        }
    }

    private void displayReport(GameReport report){
        currentPhase = report.getPhase();
        halos = new ArrayList<>();

        //Display my cards and the zoomed hidden ones:
        ArrayList<Card> myC = report.getMyCards();
        int len = myC.size();
        //containerZoomedCard.removeAll();
        //containerCard.removeAll();
        ArrayList<Component> zoomedCards = new ArrayList<>();
        ArrayList<Component> smallCards = new ArrayList<>();
        for(int i=0; i<len; i++){
            ImageIcon bcIcon = ScalingUtils.getImage(myC.get(i).getFront());
            Image bcImage = bcIcon.getImage();
            Image newImg = bcImage.getScaledInstance(ScalingUtils.scaleX(105, 900), ScalingUtils.scaleY(155, 155),  Image.SCALE_SMOOTH);
            bcIcon = new ImageIcon(newImg);
            final JLabel bcLabel = new JLabel(bcIcon);
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            bcLabel.setBorder(border);
            bcLabel.setBounds(ScalingUtils.scaleX(80*i, 900), 0, ScalingUtils.scaleX(105, 900), ScalingUtils.scaleY(155, 155));
            bcLabel.setVisible(false);
            zoomedCards.add(bcLabel);

            JButton bc = new JButton();
            ImageIcon cIcon = ScalingUtils.getImage(myC.get(i).getFront());
            Image cImage = cIcon.getImage();
            newImg = cImage.getScaledInstance(ScalingUtils.scaleX(75, 900), ScalingUtils.scaleY(125, 45),  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            bc.setIcon(cIcon);
            bc.setBounds(ScalingUtils.scaleX(80*i, 900), 0, ScalingUtils.scaleX(75, 900), ScalingUtils.scaleY(125, 45));
            smallCards.add(bc);

            final int p = report.getMyCards().get(i).getPriority();
            bc.addActionListener(e->{
                try {
                    synchronized (lockWrite) {
                        //objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.reset();
                        objectOutputStream.writeObject(new PlayCardMessage(myNick, p));
                        objectOutputStream.flush();
                        startLoading();
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            bc.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    containerZoomedCard.setVisible(true);
                    bcLabel.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    containerZoomedCard.setVisible(false);
                    bcLabel.setVisible(false);
                }
            });
            bc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        containerCard.removeAll();
        for(Component c : smallCards){
            containerCard.add(c);
        }
        containerCard.repaint();

        containerZoomedCard.removeAll();
        for(Component c : zoomedCards){
            containerZoomedCard.add(c);
        }
        containerZoomedCard.repaint();

        //Show played card:
        if(report.getMyLastCard() != null){
            containerLastCard.removeAll();
            JLabel c = new JLabel();
            ImageIcon cIcon = ScalingUtils.getImage(report.getMyLastCard().getFront());
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145),  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            c.setIcon(cIcon);
            c.setBounds(0, 0, ScalingUtils.scaleX(85, 91), ScalingUtils.scaleY(125, 145));
            containerLastCard.add(c);
            containerLastCard.repaint();
        }

        //Display all islands:
        //containerIslands.removeAll();
        LinkedList<Island> allI = report.getAllIslands();
        len = allI.size();
        ArrayList<Component> islands = new ArrayList<>();
        for(int i=0; i<len; i++){
            final int idIsland = Character.getNumericValue(report.getAllIslands().get(i).toString().charAt(0))*10+Character.getNumericValue(report.getAllIslands().get(i).toString().charAt(1));
            final int posIsland = i;

            JPanel is = new JPanel();
            is.setLayout(null);
            is.setOpaque(false);
            if(i<5) is.setBounds(ScalingUtils.scaleX(140*i, 700), 0, ScalingUtils.scaleX(140, 700), ScalingUtils.scaleY(140, 420));
            else if(i<7) is.setBounds(ScalingUtils.scaleX(560, 700), ScalingUtils.scaleY(140+140*(i-5), 420), ScalingUtils.scaleX(140, 700), ScalingUtils.scaleY(140, 420));
            else if(i<11) is.setBounds(ScalingUtils.scaleX(420-140*(i-7), 700), ScalingUtils.scaleY(280, 420), ScalingUtils.scaleX(140, 700), ScalingUtils.scaleY(140, 420));
            else is.setBounds(0, ScalingUtils.scaleY(140, 420), ScalingUtils.scaleX(140, 700), ScalingUtils.scaleY(140, 420));
            containerIslands.add(is);

            final int numIslands = report.getAllIslands().size();

            int widthIsland = is.getWidth();
            int heightIsland = is.getHeight();

            JButton iButton = new JButton();
            iButton.setContentAreaFilled(false);
            iButton.setBorderPainted(false);
            iButton.setBounds(0, 0, ScalingUtils.scaleX(140, widthIsland), ScalingUtils.scaleY(140, heightIsland));
            is.add(iButton);

            if(allI.get(i).getProhibition()){
                JLabel soc = new JLabel();
                ImageIcon socIcon = ScalingUtils.getImage("/Block.png");
                Image socImage = socIcon.getImage();
                Image newImg = socImage.getScaledInstance(ScalingUtils.scaleX(26, widthIsland),ScalingUtils.scaleY(26, heightIsland),Image.SCALE_SMOOTH);
                socIcon = new ImageIcon(newImg);
                soc.setIcon(socIcon);
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                soc.setBorder(border);
                soc.setBounds(ScalingUtils.scaleX(4, widthIsland), ScalingUtils.scaleY(110, heightIsland), ScalingUtils.scaleX(26, widthIsland), ScalingUtils.scaleY(26, heightIsland));
                is.add(soc);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.BLUE) > 0){
                JLabel sb = new JLabel();
                ImageIcon sbIcon = ScalingUtils.getImage("/Stud_blue.png");
                Image sbImage = sbIcon.getImage();
                Image newImg =sbImage.getScaledInstance(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland),  Image.SCALE_SMOOTH);
                sbIcon = new ImageIcon(newImg);
                sb.setIcon(sbIcon);
                sb.setOpaque(false);
                sb.setBounds(ScalingUtils.scaleX(15, widthIsland), ScalingUtils.scaleY(60, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland));
                is.add(sb);
                JLabel b = new JLabel();
                b.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.BLUE));
                b.setHorizontalAlignment(SwingConstants.CENTER);
                b.setVerticalAlignment(SwingConstants.CENTER);
                b.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                b.setBounds(ScalingUtils.scaleX(15, widthIsland), ScalingUtils.scaleY(90, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(b);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.YELLOW) > 0){
                JLabel sy = new JLabel();
                ImageIcon syIcon = ScalingUtils.getImage("/Stud_yellow.png");
                Image syImage = syIcon.getImage();
                Image newImg =syImage.getScaledInstance(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland),  Image.SCALE_SMOOTH);
                syIcon = new ImageIcon(newImg);
                sy.setIcon(syIcon);
                sy.setOpaque(false);
                sy.setBounds(ScalingUtils.scaleX(55, widthIsland), ScalingUtils.scaleY(60, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland));
                is.add(sy);
                JLabel y = new JLabel();
                y.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.YELLOW));
                y.setHorizontalAlignment(SwingConstants.CENTER);
                y.setVerticalAlignment(SwingConstants.CENTER);
                y.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                y.setBounds(ScalingUtils.scaleX(55, widthIsland), ScalingUtils.scaleY(90, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(y);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.RED) > 0){
                JLabel sr = new JLabel();
                ImageIcon srIcon = ScalingUtils.getImage("/Stud_red.png");
                Image srImage = srIcon.getImage();
                Image newImg =srImage.getScaledInstance(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland),  Image.SCALE_SMOOTH);
                srIcon = new ImageIcon(newImg);
                sr.setIcon(srIcon);
                sr.setOpaque(false);
                sr.setBounds(ScalingUtils.scaleX(95, widthIsland), ScalingUtils.scaleY(60, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland));
                is.add(sr);
                JLabel r = new JLabel();
                r.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.RED));
                r.setHorizontalAlignment(SwingConstants.CENTER);
                r.setVerticalAlignment(SwingConstants.CENTER);
                r.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                r.setBounds(ScalingUtils.scaleX(95, widthIsland), ScalingUtils.scaleY(90, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(r);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.GREEN) > 0){
                JLabel sg = new JLabel();
                ImageIcon sgIcon = ScalingUtils.getImage("/Stud_green.png");
                Image sgImage = sgIcon.getImage();
                Image newImg =sgImage.getScaledInstance(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland),  Image.SCALE_SMOOTH);
                sgIcon = new ImageIcon(newImg);
                sg.setIcon(sgIcon);
                sg.setOpaque(false);
                sg.setBounds(ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(100, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland));
                is.add(sg);
                JLabel g = new JLabel();
                g.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.GREEN));
                g.setHorizontalAlignment(SwingConstants.CENTER);
                g.setVerticalAlignment(SwingConstants.CENTER);
                g.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                g.setBounds(ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(130, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(g);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.PINK) > 0){
                JLabel sp = new JLabel();
                ImageIcon spIcon = ScalingUtils.getImage("/Stud_pink.png");
                Image spImage = spIcon.getImage();
                Image newImg =spImage.getScaledInstance(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland),  Image.SCALE_SMOOTH);
                spIcon = new ImageIcon(newImg);
                sp.setIcon(spIcon);
                sp.setOpaque(false);
                sp.setBounds(ScalingUtils.scaleX(75, widthIsland), ScalingUtils.scaleY(100, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(30, heightIsland));
                is.add(sp);
                JLabel p = new JLabel();
                p.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.PINK));
                p.setHorizontalAlignment(SwingConstants.CENTER);
                p.setVerticalAlignment(SwingConstants.CENTER);
                p.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                p.setBounds(ScalingUtils.scaleX(75, widthIsland), ScalingUtils.scaleY(130, heightIsland), ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(p);
            }

            //Show Mother Nature:
            if(report.getMT()==i){
                JLabel mt = new JLabel();
                ImageIcon mtIcon = ScalingUtils.getImage("/MT.png");
                posMT = i;
                Image mtImage = mtIcon.getImage();
                Image newImg =mtImage.getScaledInstance(ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(45, heightIsland),  Image.SCALE_SMOOTH);
                mtIcon = new ImageIcon(newImg);
                mt.setIcon(mtIcon);
                mt.setOpaque(false);
                mt.setBounds(ScalingUtils.scaleX(30, widthIsland), ScalingUtils.scaleY(5, heightIsland), ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(45, heightIsland));
                is.add(mt);
            }

            //Show tower:
            if(allI.get(i).getReport().getOwner()!=null){
                JLabel to = new JLabel();
                ImageIcon toIcon;
                switch (allI.get(i).getReport().getOwner()){
                    case WHITE -> toIcon = ScalingUtils.getImage("/Tower_white.png");
                    case BLACK -> toIcon = ScalingUtils.getImage("/Tower_black.png");
                    default -> toIcon = ScalingUtils.getImage("/Tower_grey.png");
                }
                Image toImage = toIcon.getImage();
                Image newImg = toImage.getScaledInstance(ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(45, heightIsland),  Image.SCALE_SMOOTH);
                toIcon = new ImageIcon(newImg);
                to.setIcon(toIcon);
                to.setOpaque(false);
                to.setBounds(ScalingUtils.scaleX(75, widthIsland), ScalingUtils.scaleY(5, heightIsland), ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(45, heightIsland));
                is.add(to);
                JLabel nt = new JLabel();
                nt.setText("x"+allI.get(i).getReport().getTowerNumbers());
                nt.setHorizontalAlignment(SwingConstants.CENTER);
                nt.setVerticalAlignment(SwingConstants.CENTER);
                nt.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
                nt.setBounds(ScalingUtils.scaleX(75, widthIsland), ScalingUtils.scaleY(50, heightIsland), ScalingUtils.scaleX(35, widthIsland), ScalingUtils.scaleY(10, heightIsland));
                is.add(nt);
            }

            JLabel island = new JLabel();
            ImageIcon islandIcon = ScalingUtils.getImage(allI.get(i).getSprite());
            Image islandImage = islandIcon.getImage();
            Image newImg = islandImage.getScaledInstance(ScalingUtils.scaleX(140, widthIsland), ScalingUtils.scaleY(140, heightIsland),  Image.SCALE_SMOOTH);
            islandIcon = new ImageIcon(newImg);
            island.setIcon(islandIcon);
            island.setOpaque(false);
            island.setBounds(0, 0, ScalingUtils.scaleX(140, widthIsland), ScalingUtils.scaleY(140, heightIsland));
            is.add(island);

            JLabel halo = new JLabel();
            ImageIcon haloIcon = ScalingUtils.getImage("/Halo.png");
            Image haloImage = haloIcon.getImage();
            newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(140, widthIsland), ScalingUtils.scaleY(140, heightIsland),  Image.SCALE_SMOOTH);
            haloIcon = new ImageIcon(newImg);
            halo.setIcon(haloIcon);
            halo.setOpaque(false);
            halo.setBounds(0, 0, ScalingUtils.scaleX(140, widthIsland), ScalingUtils.scaleY(140, heightIsland));
            halo.setVisible(false);
            is.add(halo);

            iButton.addActionListener(e->{
                if(currentPhase==Phase.MIDDLETURN){
                    if(idStudentToMove != -1){
                        try {
                            synchronized (lockWrite) {
                                //objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.ISLAND, idIsland));
                                objectOutputStream.flush();
                                startLoading();
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                }
                else if(currentPhase==Phase.MOVEMNTURN){
                    try {
                        synchronized (lockWrite) {
                            //objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new MoveMotherNatureMessage(myNick, (numIslands + posIsland - posMT) % numIslands));
                            objectOutputStream.flush();
                            startLoading();
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(currentPhase==Phase.ACTIVECARD){
                    if(report.getChar().get(report.getActiveCard()).getId()==1){
                        try {
                            synchronized (lockWrite) {
                                //objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.ISLAND, idIsland));
                                objectOutputStream.flush();
                                startLoading();
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                    else if(report.getChar().get(report.getActiveCard()).getId()==3){
                        try {
                            synchronized (lockWrite) {
                                //objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(new IslandInfluenceMessage(myNick, idIsland));
                                objectOutputStream.flush();
                                startLoading();
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                    else if(report.getChar().get(report.getActiveCard()).getId()==5){
                        try {
                            synchronized (lockWrite) {
                                //objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(new BlockIslandMessage(myNick, idIsland));
                                objectOutputStream.flush();
                                startLoading();
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                }
            });
            iButton.addMouseListener(new MouseAdapter() {
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
            iButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            islands.add(is);
        }

        //Display clouds:
        ArrayList<ArrayList<Student>> allS = report.getStudentsOnClouds();
        len = allS.size();
        for(int i=0; i<len; i++){
            JButton clButton = new JButton();
            clButton.setContentAreaFilled(false);
            clButton.setBorderPainted(false);
            clButton.setBounds(ScalingUtils.scaleX(150+105*i, 700), ScalingUtils.scaleY(160, 420), ScalingUtils.scaleX(100, 700), ScalingUtils.scaleY(100, 420));
            islands.add(clButton);

            JPanel cl = new JPanel();
            cl.setOpaque(false);
            cl.setLayout(null);
            cl.setBounds(ScalingUtils.scaleX(150+105*i, 700), ScalingUtils.scaleY(160, 420), ScalingUtils.scaleX(100, 700), ScalingUtils.scaleY(100, 420));

            int widthCloud = cl.getWidth();
            int heightCloud = cl.getHeight();

            ImageIcon clIcon;
            if(report.getNumPlayers()==3){
                if(i==0) clIcon = ScalingUtils.getImage("/CloudX4_1.png");
                else if(i==1) clIcon = ScalingUtils.getImage("/CloudX4_2.png");
                else clIcon = ScalingUtils.getImage("/CloudX4_3.png");

                for(int j=0; j<allS.get(i).size(); j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon = ScalingUtils.getImage(allS.get(i).get(j).getSprite());
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud),  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(ScalingUtils.scaleX(5, widthCloud), ScalingUtils.scaleY(15, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                        case 1 -> st.setBounds(ScalingUtils.scaleX(54, widthCloud), ScalingUtils.scaleY(5, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                        case 2 -> st.setBounds(ScalingUtils.scaleX(59, widthCloud), ScalingUtils.scaleY(56, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                        case 3 -> st.setBounds(ScalingUtils.scaleX(11, widthCloud), ScalingUtils.scaleY(60, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                    }
                    cl.add(st);
                }
            }
            else{
                if(i==0) clIcon = ScalingUtils.getImage("/CloudX3_1.png");
                else if(i==1) clIcon = ScalingUtils.getImage("/CloudX3_2.png");
                else if(i==2) clIcon = ScalingUtils.getImage("/CloudX3_3.png");
                else clIcon = ScalingUtils.getImage("/CloudX3_4.png");

                for(int j=0; j<allS.get(i).size(); j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon = ScalingUtils.getImage(allS.get(i).get(j).getSprite());
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud),  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(ScalingUtils.scaleX(8, widthCloud), ScalingUtils.scaleY(27, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                        case 1 -> st.setBounds(ScalingUtils.scaleX(54, widthCloud), ScalingUtils.scaleY(13, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                        case 2 -> st.setBounds(ScalingUtils.scaleX(44, widthCloud), ScalingUtils.scaleY(61, heightCloud), ScalingUtils.scaleX(30, widthCloud), ScalingUtils.scaleY(30, heightCloud));
                    }
                    cl.add(st);
                }
            }
            islands.add(cl);

            JLabel cloud = new JLabel();
            Image cloudImage = clIcon.getImage();
            Image newImg = cloudImage.getScaledInstance(ScalingUtils.scaleX(100, widthCloud), ScalingUtils.scaleY(100, heightCloud),  Image.SCALE_SMOOTH);
            clIcon = new ImageIcon(newImg);
            cloud.setIcon(clIcon);
            cloud.setOpaque(false);
            cloud.setBounds(0, 0, ScalingUtils.scaleX(100, widthCloud), ScalingUtils.scaleY(100, heightCloud));
            cl.add(cloud);

            JLabel halo = new JLabel();
            ImageIcon haloIcon = ScalingUtils.getImage("/Halo.png");
            Image haloImage = haloIcon.getImage();
            newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(100, widthCloud), ScalingUtils.scaleY(100, heightCloud),  Image.SCALE_SMOOTH);
            haloIcon = new ImageIcon(newImg);
            halo.setIcon(haloIcon);
            halo.setOpaque(false);
            halo.setBounds(0, 0, ScalingUtils.scaleX(100, widthCloud), ScalingUtils.scaleY(100, heightCloud));
            halo.setVisible(false);
            cl.add(halo);

            final int posCloud = i;
            clButton.addActionListener(e->{
                try {
                    synchronized (lockWrite) {
                        //objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.reset();
                        objectOutputStream.writeObject(new SelectCloudMessage(myNick, posCloud));
                        objectOutputStream.flush();
                        startLoading();
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            clButton.addMouseListener(new MouseAdapter() {
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
            clButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        containerIslands.removeAll();
        for(Component c : islands){
            containerIslands.add(c);
        }
        containerIslands.repaint();

        //containerIslands.repaint();

        //Display info:
        containerInfo.removeAll();
        JLabel name = new JLabel();
        name.setText(report.getTurnOf());
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setVerticalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        name.setBounds(0,0, ScalingUtils.scaleX(105, 180), ScalingUtils.scaleY(20, 40));
        containerInfo.add(name);
        JLabel phase = new JLabel();
        //phase.setText(report.getCurrentPhase());
        if(report.getPhase()==Phase.PRETURN) phase.setText("Choose a card");
        else if(report.getPhase()==Phase.MIDDLETURN) phase.setText("Move x"+report.getRemainingMoves()+" students");
        else if(report.getPhase()==Phase.MOVEMNTURN) phase.setText("Move Mother Nature");
        else if(report.getPhase()==Phase.ENDTURN) phase.setText("Select a cloud");
        else if(report.getPhase()==Phase.ACTIVECARD) phase.setText("Activating card");
        phase.setHorizontalAlignment(SwingConstants.CENTER);
        phase.setVerticalAlignment(SwingConstants.CENTER);
        phase.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(9)));
        phase.setBounds(0, ScalingUtils.scaleY(20, 40), ScalingUtils.scaleX(105, 180), ScalingUtils.scaleY(20, 40));
        containerInfo.add(phase);
        containerInfo.repaint();

        //Display my dashboard:
        containerDash.removeAll();
        len = report.getMyEntrance().size();
        for(int i=0; i<len; i++){
            JButton st = new JButton();
            ImageIcon stIcon = ScalingUtils.getImage(report.getMyEntrance().get(i).getSprite());
            Image stImage = stIcon.getImage();
            Image newImg = stImage.getScaledInstance(ScalingUtils.scaleX(30, 500), ScalingUtils.scaleY(30, 200),  Image.SCALE_SMOOTH);
            stIcon = new ImageIcon(newImg);
            st.setIcon(stIcon);
            st.setContentAreaFilled(false);
            st.setBorderPainted(false);
            st.setBounds(ScalingUtils.scaleX(10+30*((i+1)%2), 500), ScalingUtils.scaleY(20+31*((i+1)/2), 200),  ScalingUtils.scaleX(30, 500), ScalingUtils.scaleY(30, 200));
            st.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            containerDash.add(st);

            JLabel halo = new JLabel();
            ImageIcon haloIcon = ScalingUtils.getImage("/Halo.png");
            Image haloImage = haloIcon.getImage();
            newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 500), ScalingUtils.scaleY(40, 200),  Image.SCALE_SMOOTH);
            haloIcon = new ImageIcon(newImg);
            halo.setIcon(haloIcon);
            halo.setOpaque(false);
            halo.setBounds(ScalingUtils.scaleX(5+30*((i+1)%2), 500), ScalingUtils.scaleY(20+31*((i+1)/2), 200), ScalingUtils.scaleX(40, 500), ScalingUtils.scaleY(40, 200));
            halo.setVisible(false);
            containerDash.add(halo);
            halos.add(halo);
            JLabel halo2 = new JLabel();
            ImageIcon haloIcon2 = ScalingUtils.getImage("/Halo.png");
            Image haloImage2 = haloIcon2.getImage();
            newImg =haloImage2.getScaledInstance(ScalingUtils.scaleX(40, 500), ScalingUtils.scaleY(40, 200),  Image.SCALE_SMOOTH);
            haloIcon2 = new ImageIcon(newImg);
            halo2.setIcon(haloIcon2);
            halo2.setOpaque(false);
            halo2.setBounds(ScalingUtils.scaleX(5+30*((i+1)%2), 500), ScalingUtils.scaleY(20+31*((i+1)/2), 200), ScalingUtils.scaleX(40, 500), ScalingUtils.scaleY(40, 200));
            halo2.setVisible(false);
            containerDash.add(halo2);

            final int idStudent = report.getMyEntrance().get(i).getId();
            final it.polimi.ingsw.model.Color colorStudent = report.getMyEntrance().get(i).getColor();
            st.addActionListener(e-> {
                idStudentToMove = idStudent;
                colorStudentToMove = colorStudent;
                departureType = Location.ENTRANCE;
                departureID = -1;
                turnOffAllHalos();
                halo.setVisible(true);
            });
            st.addMouseListener(new MouseAdapter() {
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
        }

        for(int i=0; i<5; i++){
            JButton taButton = new JButton();
            taButton.setContentAreaFilled(false);
            taButton.setBorderPainted(false);
            taButton.setBounds(ScalingUtils.scaleX(95, 500), ScalingUtils.scaleY(22+32*i, 200), ScalingUtils.scaleX(24*10, 500), ScalingUtils.scaleY(23, 200));
            taButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            containerDash.add(taButton);

            it.polimi.ingsw.model.Color colorTable;
            switch (i){
                case 0 -> colorTable = it.polimi.ingsw.model.Color.GREEN;
                case 1 -> colorTable = it.polimi.ingsw.model.Color.RED;
                case 2 -> colorTable = it.polimi.ingsw.model.Color.YELLOW;
                case 3 -> colorTable = it.polimi.ingsw.model.Color.PINK;
                default -> colorTable = it.polimi.ingsw.model.Color.BLUE;
            }
            final int pos = i;
            taButton.addActionListener(e->{
                if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==9){
                    try {
                        synchronized (lockWrite) {
                            //objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new BlockColorMessage(myNick, colorTable));
                            objectOutputStream.flush();
                            startLoading();
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==10 && report.getMyCanteen().get(pos)>0){
                    try {
                        synchronized (lockWrite) {
                            //objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new ExchangeStudentMessage(myNick, idStudentToMove, report.getLastMyCanteen().get(pos), Location.ENTRANCE, -1, Location.CANTEEN, -1));
                            objectOutputStream.flush();
                            startLoading();
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==12){
                    try {
                        synchronized (lockWrite) {
                            //objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new PutBackMessage(myNick, colorTable));
                            objectOutputStream.flush();
                            startLoading();
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(colorStudentToMove==colorTable){
                    try {
                        synchronized (lockWrite) {
                            //objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.CANTEEN, -1));
                            objectOutputStream.flush();
                            startLoading();
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                    colorStudentToMove = null;
                }
            });

            len = report.getMyCanteen().get(i);
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
        len = report.getMyTowers();
        ColorTower myCT = report.getMyColor();
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
            Image newImg = toImage.getScaledInstance(ScalingUtils.scaleX(35, 500), ScalingUtils.scaleY(45, 200),  Image.SCALE_SMOOTH);
            toIcon = new ImageIcon(newImg);
            to.setIcon(toIcon);
            to.setOpaque(false);
            if(report.getNumPlayers()==3) to.setBounds(ScalingUtils.scaleX(405+35*(i%2), 500), ScalingUtils.scaleY(85-30*(i/2), 200), ScalingUtils.scaleX(35, 500), ScalingUtils.scaleY(45, 200));
            else to.setBounds(ScalingUtils.scaleX(405+35*(i%2), 500), ScalingUtils.scaleY(115-30*(i/2), 200), ScalingUtils.scaleX(35, 500), ScalingUtils.scaleY(45, 200));
            containerDash.add(to);
        }
        it.polimi.ingsw.model.Color[] colInDashC = {it.polimi.ingsw.model.Color.GREEN, it.polimi.ingsw.model.Color.RED, it.polimi.ingsw.model.Color.YELLOW, it.polimi.ingsw.model.Color.PINK, it.polimi.ingsw.model.Color.BLUE};
        String[] colInDashS = {"/Prof_green.png", "/Prof_red.png", "/Prof_yellow.png", "/Prof_pink.png", "/Prof_blue.png"};
        for(int i=0; i<5; i++){
            if(report.getProfessors().get(colInDashC[i]).equals(report.getNamePlayer())){
                JLabel pro = new JLabel();
                ImageIcon proIcon = ScalingUtils.getImage(colInDashS[i]);
                Image proImage = proIcon.getImage();
                Image newImg = proImage.getScaledInstance(ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200),  Image.SCALE_SMOOTH);
                proIcon = new ImageIcon(newImg);
                pro.setIcon(proIcon);
                pro.setOpaque(false);
                pro.setBounds(ScalingUtils.scaleX(352, 500), ScalingUtils.scaleY(22+32*i, 200), ScalingUtils.scaleX(23, 500), ScalingUtils.scaleY(23, 200));
                containerDash.add(pro);
            }
        }
        containerDash.repaint();

        //Show coins:
        numC.setText("x"+report.getMyCoins());

        //Enable/disable character'cards if it was activated in the previous move:
        if(report.getActiveCard()!=-1) characterButtons.get(report.getActiveCard()).setVisible(false);
        else{
            for (JButton cb : characterButtons){
                if(!cb.isVisible()) cb.setVisible(true);
            }
        }

        //Show students on card (if available):
        for(int i=0; i<report.getChar().size(); i++){
            containerCharacters.get(i).removeAll();

            if(report.getChar().get(i).getTypeCharacter() == CharacterType.EXCHANGE){
                MovementCharacter mc = (MovementCharacter)report.getChar().get(i);
                ArrayList<Student> studs = mc.getStudents();
                for(int j=0; j<studs.size(); j++){
                    JButton soc = new JButton();
                    ImageIcon socIcon = ScalingUtils.getImage(studs.get(j).getSprite());
                    Image socImage = socIcon.getImage();
                    Image newImg = socImage.getScaledInstance(ScalingUtils.scaleX(30, 145), ScalingUtils.scaleY(30, 105),  Image.SCALE_SMOOTH);
                    socIcon = new ImageIcon(newImg);
                    soc.setIcon(socIcon);
                    soc.setContentAreaFilled(false);
                    soc.setBorderPainted(false);
                    soc.setBounds(ScalingUtils.scaleX(22+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+55, 105), ScalingUtils.scaleX(30, 145), ScalingUtils.scaleY(30, 105));
                    containerCharacters.get(i).add(soc);

                    JLabel halo = new JLabel();
                    ImageIcon haloIcon = ScalingUtils.getImage("/Halo.png");
                    Image haloImage = haloIcon.getImage();
                    newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105),  Image.SCALE_SMOOTH);
                    haloIcon = new ImageIcon(newImg);
                    halo.setIcon(haloIcon);
                    halo.setOpaque(false);
                    halo.setBounds(ScalingUtils.scaleX(17+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+55, 105), ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105));
                    halo.setVisible(false);
                    containerCharacters.get(i).add(halo);

                    final int idStudent = studs.get(j).getId();
                    final it.polimi.ingsw.model.Color colorStudent = studs.get(j).getColor();
                    Location prov = Location.CARD_ISLAND;
                    if(report.getChar().get(i).getId()==1) prov = Location.CARD_ISLAND;
                    else if(report.getChar().get(i).getId()==7) prov = Location.CARD_EXCHANGE;
                    else if(report.getChar().get(i).getId()==11) prov = Location.CARD_CANTEEN;
                    final Location dest = prov;
                    soc.addActionListener(e-> {
                        try {
                            synchronized (lockWrite) {
                                //objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(new ExchangeStudentMessage(myNick, idStudentToMove, idStudent, Location.ENTRANCE, -1, Location.CARD_EXCHANGE, -1));
                                objectOutputStream.flush();
                                startLoading();
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    });
                    soc.addMouseListener(new MouseAdapter() {
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
                    soc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            else if(report.getChar().get(i).getTypeCharacter() == CharacterType.MOVEMENT){
                MovementCharacter mc = (MovementCharacter)report.getChar().get(i);
                ArrayList<Student> studs = mc.getStudents();
                for(int j=0; j<studs.size(); j++){
                    JButton soc = new JButton();
                    ImageIcon socIcon = ScalingUtils.getImage(studs.get(j).getSprite());
                    Image socImage = socIcon.getImage();
                    Image newImg = socImage.getScaledInstance(ScalingUtils.scaleX(30, 145), ScalingUtils.scaleY(30, 105), Image.SCALE_SMOOTH);
                    socIcon = new ImageIcon(newImg);
                    soc.setIcon(socIcon);
                    soc.setContentAreaFilled(false);
                    soc.setBorderPainted(false);
                    soc.setBounds(ScalingUtils.scaleX(22+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+55, 105), ScalingUtils.scaleX(30, 145), ScalingUtils.scaleY(30, 105));
                    containerCharacters.get(i).add(soc);

                    JLabel halo = new JLabel();
                    ImageIcon haloIcon = ScalingUtils.getImage("/Halo.png");
                    Image haloImage = haloIcon.getImage();
                    newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105),  Image.SCALE_SMOOTH);
                    haloIcon = new ImageIcon(newImg);
                    halo.setIcon(haloIcon);
                    halo.setOpaque(false);;
                    halo.setBounds(ScalingUtils.scaleX(17+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+55, 105), ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105));
                    halo.setVisible(false);
                    containerCharacters.get(i).add(halo);
                    halos.add(halo);
                    JLabel halo2 = new JLabel();
                    ImageIcon haloIcon2 = ScalingUtils.getImage("/Halo.png");
                    Image haloImage2 = haloIcon2.getImage();
                    newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105), Image.SCALE_SMOOTH);
                    haloIcon2 = new ImageIcon(newImg);
                    halo2.setIcon(haloIcon2);
                    halo2.setOpaque(false);;
                    halo2.setBounds(ScalingUtils.scaleX(17+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+55, 105), ScalingUtils.scaleX(40, 145), ScalingUtils.scaleY(40, 105));
                    halo2.setVisible(false);
                    containerCharacters.get(i).add(halo2);

                    final int idStudent = studs.get(j).getId();
                    final it.polimi.ingsw.model.Color colorStudent = studs.get(j).getColor();
                    Location prov = Location.CARD_ISLAND;
                    if(report.getChar().get(i).getId()==1) prov = Location.CARD_ISLAND;
                    else if(report.getChar().get(i).getId()==7) prov = Location.CARD_EXCHANGE;
                    else if(report.getChar().get(i).getId()==11) prov = Location.CARD_CANTEEN;
                    final Location dest = prov;
                    soc.addActionListener(e-> {
                        idStudentToMove = idStudent;
                        colorStudentToMove = colorStudent;
                        departureType = dest;
                        departureID = -1;
                        turnOffAllHalos();
                        halo.setVisible(true);
                    });
                    soc.addMouseListener(new MouseAdapter() {
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
                    soc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            else if(report.getChar().get(i).getTypeCharacter() == CharacterType.ACTION){
                ActionCharacter ac = (ActionCharacter) report.getChar().get(i);
                for(int j=0; j<ac.getNumTokens(); j++){
                    JLabel soc = new JLabel();
                    ImageIcon socIcon = ScalingUtils.getImage("/Block.png");
                    Image socImage = socIcon.getImage();
                    Image newImg = socImage.getScaledInstance(ScalingUtils.scaleX(26, 145), ScalingUtils.scaleY(26, 105), Image.SCALE_SMOOTH);
                    socIcon = new ImageIcon(newImg);
                    soc.setIcon(socIcon);
                    Border border = BorderFactory.createLineBorder(Color.BLACK);
                    soc.setBorder(border);
                    soc.setBounds(ScalingUtils.scaleX(22+j%2*30, 145), ScalingUtils.scaleY((j/2)*30+70, 105), ScalingUtils.scaleX(26, 145), ScalingUtils.scaleY(26, 105));
                    containerCharacters.get(i).add(soc);
                }
            }

            //Add coins if selected:
            int addedValue = report.getChar().get(i).getCost() - report.getChar().get(i).getOriginalCost();
            for(int j=0; j<addedValue; j++){
                JLabel mo = new JLabel();
                ImageIcon moIcon = ScalingUtils.getImage("/AddCoin.png");
                Image moImage = moIcon.getImage();
                Image newImg = moImage.getScaledInstance(ScalingUtils.scaleX(25, 145), ScalingUtils.scaleY(15, 105),  Image.SCALE_SMOOTH);
                moIcon = new ImageIcon(newImg);
                mo.setIcon(moIcon);
                mo.setOpaque(false);
                mo.setBounds(ScalingUtils.scaleX(5, 145), ScalingUtils.scaleY(18+j*10, 105), ScalingUtils.scaleX(25, 145), ScalingUtils.scaleY(15, 105));
                containerCharacters.get(i).add(mo);
            }

            containerCharacters.get(i).repaint();
        }

        //Opponents'dialogs:
        for(int i=0; i<report.getNumPlayers(); i++){
            if(!report.getOpponentsNick().get(i).equals(myNick)){
                OD[i].updateDialog(report, i);
            }
        }

        layered.setLayer(loading, 1);
        layered.repaint();
    }



    private void buildHelpScreen(JPanel helpScreen, int numPlayers){
        //900x705

        //My cards'panel:
        JPanel cards = new JPanel();
        cards.setBounds(ScalingUtils.scaleX(80, 900), ScalingUtils.scaleY(625, 705), ScalingUtils.scaleX(914, 900), ScalingUtils.scaleY(59, 705));
        cards.setLayout(null);
        cards.setOpaque(false);
        Border border = BorderFactory.createLineBorder(Color.YELLOW, 7, true);
        cards.setBorder(border);
        helpScreen.add(cards);

        JLabel cardsT = new JLabel("Click on a card to play it", JLabel.CENTER);
        cardsT.setOpaque(true);
        cardsT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        cardsT.setBackground(new Color(128,128,128,255));
        cardsT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardsT.setBounds(ScalingUtils.scaleX(140, 900), ScalingUtils.scaleY(640, 705), ScalingUtils.scaleX(145, 900), ScalingUtils.scaleY(20, 705));
        helpScreen.add(cardsT);

        //My entrance:
        JLabel entranceT = new JLabel("<html>Click on a student<br>to select it</html>", JLabel.CENTER);
        entranceT.setOpaque(true);
        entranceT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        entranceT.setBackground(new Color(128,128,128,255));
        entranceT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        entranceT.setBounds(ScalingUtils.scaleX(52, 900), ScalingUtils.scaleY(470, 705), ScalingUtils.scaleX(95, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(entranceT);

        JPanel entrance = new JPanel();
        entrance.setBounds(ScalingUtils.scaleX(55, 900), ScalingUtils.scaleY(440, 705), ScalingUtils.scaleX(80, 900), ScalingUtils.scaleY(178, 705));
        entrance.setLayout(null);
        entrance.setOpaque(false);
        entrance.setBorder(border);
        helpScreen.add(entrance);

        //My tables:
        JLabel profT = new JLabel("<html>Here're your<br>professors</html>", JLabel.CENTER);
        profT.setOpaque(true);
        profT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        profT.setBackground(new Color(128,128,128,255));
        profT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        profT.setBounds(ScalingUtils.scaleX(387, 900), ScalingUtils.scaleY(470, 705), ScalingUtils.scaleX(70, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(profT);

        JPanel tables = new JPanel();
        tables.setBounds(ScalingUtils.scaleX(136, 900), ScalingUtils.scaleY(440, 705), ScalingUtils.scaleX(259, 900), ScalingUtils.scaleY(178, 705));
        tables.setLayout(null);
        tables.setOpaque(false);
        tables.setBorder(border);
        helpScreen.add(tables);

        JLabel tablesT = new JLabel("<html>Click on a table to place<br>there a selected student</html>", JLabel.CENTER);
        tablesT.setOpaque(true);
        tablesT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        tablesT.setBackground(new Color(128,128,128,255));
        tablesT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tablesT.setBounds(ScalingUtils.scaleX(196, 900), ScalingUtils.scaleY(500, 705), ScalingUtils.scaleX(130, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(tablesT);

        //My prof:
        JPanel prof = new JPanel();
        prof.setBounds(ScalingUtils.scaleX(397, 900), ScalingUtils.scaleY(440, 705), ScalingUtils.scaleX(47, 900), ScalingUtils.scaleY(178, 705));
        prof.setLayout(null);
        prof.setOpaque(false);
        prof.setBorder(border);
        helpScreen.add(prof);

        //My towers:
        JLabel towersT = new JLabel("<html>Here're your<br>team's towers</html>", JLabel.CENTER);
        towersT.setOpaque(true);
        towersT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        towersT.setBackground(new Color(128,128,128,255));
        towersT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        towersT.setBounds(ScalingUtils.scaleX(455, 900), ScalingUtils.scaleY(520, 705), ScalingUtils.scaleX(80, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(towersT);

        JPanel towers = new JPanel();
        towers.setBounds(ScalingUtils.scaleX(450, 900), ScalingUtils.scaleY(445, 705), ScalingUtils.scaleX(91, 900), ScalingUtils.scaleY(153, 705));
        towers.setLayout(null);
        towers.setOpaque(false);
        towers.setBorder(border);
        helpScreen.add(towers);

        //My coins:
        JLabel coinsT = new JLabel("<html>Here're<br>your coins</html>", JLabel.CENTER);
        coinsT.setOpaque(true);
        coinsT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        coinsT.setBackground(new Color(128,128,128,255));
        coinsT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        coinsT.setBounds(ScalingUtils.scaleX(642, 900), ScalingUtils.scaleY(405, 705), ScalingUtils.scaleX(60, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(coinsT);

        JPanel coins = new JPanel();
        coins.setBounds(ScalingUtils.scaleX(560, 900), ScalingUtils.scaleY(425, 705), ScalingUtils.scaleX(101, 900), ScalingUtils.scaleY(30, 705));
        coins.setLayout(null);
        coins.setOpaque(false);
        coins.setBorder(border);
        helpScreen.add(coins);

        //My last card:
        JLabel lastT = new JLabel("<html>The last card<br>you played</html>", JLabel.CENTER);
        lastT.setOpaque(true);
        lastT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        lastT.setBackground(new Color(128,128,128,255));
        lastT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lastT.setBounds(ScalingUtils.scaleX(572, 900), ScalingUtils.scaleY(515, 705), ScalingUtils.scaleX(75, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(lastT);

        JPanel last = new JPanel();
        last.setBounds(ScalingUtils.scaleX(560, 900), ScalingUtils.scaleY(455, 705), ScalingUtils.scaleX(101, 900), ScalingUtils.scaleY(155, 705));
        last.setLayout(null);
        last.setOpaque(false);
        last.setBorder(border);
        helpScreen.add(last);

        //Info:
        JLabel infoT = new JLabel("<html>The info box displays the current<br>player's nickname and what he/she is<br>supposed to do in the current turn</html>", JLabel.CENTER);
        infoT.setOpaque(true);
        infoT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        infoT.setBackground(new Color(128,128,128,255));
        infoT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        infoT.setBounds(ScalingUtils.scaleX(565, 900), ScalingUtils.scaleY(7, 705), ScalingUtils.scaleX(190, 900), ScalingUtils.scaleY(60, 705));
        helpScreen.add(infoT);

        JPanel info = new JPanel();
        info.setBounds(ScalingUtils.scaleX(705, 900), ScalingUtils.scaleY(5, 705), ScalingUtils.scaleX(190, 900), ScalingUtils.scaleY(50, 705));
        info.setLayout(null);
        info.setOpaque(false);
        info.setBorder(border);
        helpScreen.add(info);

        //My opponents:
        JLabel opponentsT = new JLabel("<html>The opponents box displays all the<br>players according to turn's order<br>Click on the nickname of a player<br>to see his/her dashboard</html>", JLabel.CENTER);
        opponentsT.setOpaque(true);
        opponentsT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        opponentsT.setBackground(new Color(128,128,128,255));
        opponentsT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        opponentsT.setBounds(ScalingUtils.scaleX(695, 900), ScalingUtils.scaleY(83, 705), ScalingUtils.scaleX(180, 900), ScalingUtils.scaleY(65, 705));
        helpScreen.add(opponentsT);

        JPanel opponents = new JPanel();
        opponents.setBounds(ScalingUtils.scaleX(705, 900), ScalingUtils.scaleY(50, 705), ScalingUtils.scaleX(190, 900), ScalingUtils.scaleY(34+numPlayers*22, 705));
        opponents.setLayout(null);
        opponents.setOpaque(false);
        opponents.setBorder(border);
        helpScreen.add(opponents);

        //My island:
        JLabel islandT = new JLabel("<html>Click on an island to place there a selected student<br>Or click an island to move there MotherNature</html>", JLabel.CENTER);
        islandT.setOpaque(true);
        islandT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        islandT.setBackground(new Color(128,128,128,255));
        islandT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        islandT.setBounds(ScalingUtils.scaleX(102, 900), ScalingUtils.scaleY(52, 705), ScalingUtils.scaleX(260, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(islandT);

        JPanel island = new JPanel();
        island.setBounds(ScalingUtils.scaleX(2, 900), ScalingUtils.scaleY(2, 705), ScalingUtils.scaleX(148, 900), ScalingUtils.scaleY(148, 705));
        island.setLayout(null);
        island.setOpaque(false);
        island.setBorder(border);
        helpScreen.add(island);

        //My cloud:
        JLabel cloudT = new JLabel("<html>Click on a cloud to get<br>the students on it</html>", JLabel.CENTER);
        cloudT.setOpaque(true);
        cloudT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        cloudT.setBackground(new Color(128,128,128,255));
        cloudT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cloudT.setBounds(ScalingUtils.scaleX(205, 900), ScalingUtils.scaleY(202, 705), ScalingUtils.scaleX(125, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(cloudT);

        JPanel cloud = new JPanel();
        cloud.setBounds(ScalingUtils.scaleX(145, 900), ScalingUtils.scaleY(155, 705), ScalingUtils.scaleX(110, 900), ScalingUtils.scaleY(110, 705));
        cloud.setLayout(null);
        cloud.setOpaque(false);
        cloud.setBorder(border);
        helpScreen.add(cloud);

        //Characters:
        JLabel charactersT = new JLabel("<html>Click on a character to activate<br>his/her own special power</html>", JLabel.CENTER);
        charactersT.setOpaque(true);
        charactersT.setFont(new Font("MV Boli", Font.PLAIN, ScalingUtils.scaleFont(10)));
        charactersT.setBackground(new Color(128,128,128,255));
        charactersT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        charactersT.setBounds(ScalingUtils.scaleX(710, 900), ScalingUtils.scaleY(215, 705), ScalingUtils.scaleX(160, 900), ScalingUtils.scaleY(40, 705));
        helpScreen.add(charactersT);

        JPanel characters = new JPanel();
        characters.setBounds(ScalingUtils.scaleX(760, 900), ScalingUtils.scaleY(165, 705), ScalingUtils.scaleX(155, 900), ScalingUtils.scaleY(465, 705));
        characters.setLayout(null);
        characters.setOpaque(false);
        characters.setBorder(border);
        helpScreen.add(characters);
    }

    private void startLoading(){
        layered.setLayer(loading, 10);
        layered.repaint();
    }

    private void turnOffAllHalos(){
        for(int i=0; i< halos.size(); i++){
            halos.get(i).setVisible(false);
        }
    }


    /**
     * This method is used to hide the GUIGame
     */
    public void hideGUI(){
            window.setVisible(false);
    }
}