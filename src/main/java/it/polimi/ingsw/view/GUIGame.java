package it.polimi.ingsw.view;

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

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class GUIGame {
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private JFrame window = new JFrame("Eriantys"); //Main window
    private JLabel bgLabel; //Main background
    private String myNick;
    private OpponentDialog[] OD = new OpponentDialog[4];

    //Container to empty and refill every time we receive a GameReport
    private JPanel containerIslands = new JPanel();
    private JPanel containerInfo = new JPanel();
    private JPanel containerDash = new JPanel();
    private JPanel containerCard = new JPanel();
    private JPanel containerLastCard = new JPanel();
    private ArrayList<JPanel> containerCharacters = new ArrayList<>();
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
    private ArrayList<JButton> characterButtons = new ArrayList<>();


    public GUIGame(Socket socket, GameReport report, Object lockWrite) throws IOException {
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        this.lockWrite = lockWrite;

        createGUI(report);
        displayReport(report);
    }



    public int openGUI(){
        window.setVisible(true);
        while(true){
            try {
                objectInputStream = new ObjectInputStream(inputStream);
                GameReport report = (GameReport) objectInputStream.readObject();
                if(report.getError()==null) displayReport(report);
                else{
                    JOptionPane.showMessageDialog(null, report.getError(),"Eriantys - Illegal move", JOptionPane.WARNING_MESSAGE);
                    idStudentToMove = -1;
                }
                System.out.println("il valore di finished game è"+report.getFinishedGame());
                if(report.getFinishedGame()){
                    int confirm;
                    if(report.getWinner()==null){ confirm=JOptionPane.showConfirmDialog(null, "The game is finished."+" \nDo you want to play another game?", "Eriantys" ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null);}
                    else{confirm=JOptionPane.showConfirmDialog(null, "The game is finished, the winner is: "+report.getWinner()+". \nDo you want to play another game?", "Eriantys" ,JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null);}
                    return confirm;
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void createGUI(GameReport report){
        myNick = report.getNamePlayer();

        //Background image:
        ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Game_bg.jpg"));
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(900, 670,  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(900,670);

        //Main window:
        window.add(bgLabel);
        window.setSize(900, 705);
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //Show my dashboards:
        ImageIcon dashIcon = new ImageIcon(this.getClass().getResource("/Dashboard.png"));
        Image dashImage = dashIcon.getImage();
        newImg = dashImage.getScaledInstance(500, 200,  Image.SCALE_SMOOTH);
        dashIcon = new ImageIcon(newImg);
        JLabel dashLabel = new JLabel(dashIcon);
        dashLabel.setBounds(100, 430, 500, 200);
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
        coins.setBounds(605, 430, 91, 20);
        Color colorPanel = new Color(128,128,128,255);
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
        card.setBounds(605, 455, 91, 145);
        Color colorLabel = new Color(128,128,128,255);
        card.setBackground(colorLabel);
        card.setBorder(border);
        bgLabel.add(card);
        JLabel titleCa = new JLabel();
        titleCa.setText("Last played card:");
        titleCa.setHorizontalAlignment(SwingConstants.CENTER);
        titleCa.setVerticalAlignment(SwingConstants.CENTER);
        titleCa.setFont(new Font("MV Boli", Font.BOLD, 9));
        titleCa.setBounds(0,0,91,20);
        card.add(titleCa);
        containerLastCard.setLayout(null);
        containerLastCard.setBounds(3, 17, 85, 125);
        card.add(containerLastCard);
        JLabel c = new JLabel();
        ImageIcon cIcon = new ImageIcon(this.getClass().getResource("/Char_back.png"));
        Image cImage = cIcon.getImage();
        newImg = cImage.getScaledInstance(85, 125,  Image.SCALE_SMOOTH);
        cIcon = new ImageIcon(newImg);
        c.setIcon(cIcon);
        c.setBounds(0, 0, 85, 125);
        containerLastCard.add(c);

        //My cards'panel:
        JPanel myCards = new JPanel();
        myCards.setLayout(null);
        myCards.setBounds(-2, 630, 900, 45);
        myCards.setBackground(colorLabel);
        myCards.setBorder(border);
        bgLabel.add(myCards);

        JLabel titleC = new JLabel();
        titleC.setText("Your cards:");
        titleC.setHorizontalAlignment(SwingConstants.CENTER);
        titleC.setVerticalAlignment(SwingConstants.CENTER);
        titleC.setFont(new Font("MV Boli", Font.BOLD, 13));
        titleC.setBounds(5,10,75,20);
        myCards.add(titleC);

        containerCard.setBounds(90, 2, 900, 45);
        containerCard.setLayout(null);
        containerCard.setOpaque(false);
        myCards.add(containerCard);

        //Opponents'container:
        JPanel opponents = new JPanel();
        opponents.setLayout(null);
        opponents.setBounds(710, 55, 180, 24+report.getNumPlayers()*22);
        opponents.setBackground(colorLabel);
        opponents.setBorder(border);
        bgLabel.add(opponents);

        JLabel titleO = new JLabel();
        titleO.setText("All players:");
        titleO.setHorizontalAlignment(SwingConstants.CENTER);
        titleO.setVerticalAlignment(SwingConstants.CENTER);
        titleO.setFont(new Font("MV Boli", Font.BOLD, 13));
        titleO.setBounds(2,0,170,20);
        opponents.add(titleO);

        for(int i=0; i<report.getNumPlayers(); i++){
            JButton od = new JButton(report.getOpponentsNick().get(i));
            switch (report.getAllPlayersColor().get(i)){
                case WHITE -> od.setBackground(new Color(255, 255, 255, 255));
                case BLACK -> od.setBackground(new Color(0, 0, 0, 255));
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
            od.setBounds(2, 22+22*i, 170, 20);
            opponents.add(od);
            od.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        //Other infos container:
        JPanel info = new JPanel();
        info.setLayout(null);
        info.setBounds(710, 10, 180, 40);
        info.setBackground(colorLabel);
        info.setBorder(border);
        bgLabel.add(info);

        JLabel infoO = new JLabel();
        infoO.setText("Current player:");
        infoO.setHorizontalAlignment(SwingConstants.CENTER);
        infoO.setVerticalAlignment(SwingConstants.CENTER);
        infoO.setFont(new Font("MV Boli", Font.BOLD, 9));
        infoO.setBounds(2,0,70,20);
        info.add(infoO);

        JLabel info1 = new JLabel();
        info1.setText("Current phase:");
        info1.setHorizontalAlignment(SwingConstants.CENTER);
        info1.setVerticalAlignment(SwingConstants.CENTER);
        info1.setFont(new Font("MV Boli", Font.BOLD, 9));
        info1.setBounds(2,20,70,20);
        info.add(info1);

        containerInfo.setBounds(72, 0, 105, 60);
        containerInfo.setLayout(null);
        containerInfo.setOpaque(false);
        info.add(containerInfo);

        containerIslands.setBounds(5, 5, 700, 420);
        containerIslands.setLayout(null);
        containerIslands.setOpaque(false);
        bgLabel.add(containerIslands);

        //Show characters'cards if complete rules:
        for(int i=0; i<report.getChar().size(); i++){
            ImageIcon charIcon = new ImageIcon(this.getClass().getResource(report.getChar().get(i).getSprite()));
            Image charImage = charIcon.getImage();
            newImg = charImage.getScaledInstance(105, 145,  Image.SCALE_SMOOTH);
            charIcon = new ImageIcon(newImg);
            JLabel charLabel = new JLabel(charIcon);
            charLabel.setBounds(770, 150*i+175, 105, 145);
            charLabel.setBorder(border);
            bgLabel.add(charLabel);

            JButton cb = new JButton();
            cb.setContentAreaFilled(false);
            cb.setBounds(0, 0, 105, 145);
            charLabel.add(cb);
            characterButtons.add(cb);

            final int numThisCard = i;
            cb.addActionListener(e-> {
                try{
                    synchronized (lockWrite) {
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(new UsePowerMessage(myNick, numThisCard));
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cb.setToolTipText(report.getChar().get(i).getDesc_short());

            containerCharacters.add(new JPanel());
            containerCharacters.get(i).setBounds(0, 0, 105, 145);
            containerCharacters.get(i).setLayout(null);
            containerCharacters.get(i).setOpaque(false);
            charLabel.add(containerCharacters.get(i));
        }
    }



    private void displayReport(GameReport report){
        currentPhase = report.getPhase();

        //Display my cards:
        containerCard.removeAll();
        ArrayList<Card> myC = report.getMyCards();
        int len = myC.size();
        for(int i=0; i<len; i++){
            JButton bc = new JButton();
            ImageIcon cIcon = new ImageIcon(this.getClass().getResource(myC.get(i).getFront()));
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(75, 125,  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            bc.setIcon(cIcon);
            bc.setBounds(80*i, 0, 75, 125);
            containerCard.add(bc);

            final int p = report.getMyCards().get(i).getPriority();
            bc.addActionListener(e->{
                try {
                    synchronized (lockWrite) {
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(new PlayCardMessage(myNick, p));
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            bc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        containerCard.repaint();

        //Display all islands:
        containerIslands.removeAll();
        LinkedList<Island> allI = report.getAllIslands();
        len = allI.size();
        for(int i=0; i<len; i++){
            final int idIsland = Character.getNumericValue(report.getAllIslands().get(i).toString().charAt(0))*10+Character.getNumericValue(report.getAllIslands().get(i).toString().charAt(1));
            final int posIsland = i;

            JLabel is = new JLabel();
            ImageIcon isIcon = new ImageIcon(this.getClass().getResource(allI.get(i).getSprite()));
            Image isImage = isIcon.getImage();
            Image newImg = isImage.getScaledInstance(140, 140,  Image.SCALE_SMOOTH);
            isIcon = new ImageIcon(newImg);
            is.setIcon(isIcon);
            is.setOpaque(false);
            if(i<5) is.setBounds(140*i, 0, 140, 140);
            else if(i<7) is.setBounds(560, 140+140*(i-5), 140, 140);
            else if(i<11) is.setBounds(420-140*(i-7), 280, 140, 140);
            else is.setBounds(0, 140, 140, 140);
            containerIslands.add(is);

            final int numIslands = report.getAllIslands().size();

            JButton iButton = new JButton();
            iButton.setContentAreaFilled(false);
            iButton.setBorderPainted(false);
            iButton.setBounds(0, 0, 140, 140);
            is.add(iButton);

            iButton.addActionListener(e->{
                if(currentPhase==Phase.MIDDLETURN){
                    if(idStudentToMove != -1){
                        try {
                            synchronized (lockWrite) {
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.ISLAND, idIsland));
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
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new MoveMotherNatureMessage(myNick, (numIslands + posIsland - posMT) % numIslands));
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(currentPhase==Phase.ACTIVECARD){
                    if(report.getChar().get(report.getActiveCard()).getId()==1){
                        try {
                            synchronized (lockWrite) {
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.ISLAND, idIsland));
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                    else if(report.getChar().get(report.getActiveCard()).getId()==3){
                        try {
                            synchronized (lockWrite) {
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(new IslandInfluenceMessage(myNick, idIsland));
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                    else if(report.getChar().get(report.getActiveCard()).getId()==5){
                        try {
                            synchronized (lockWrite) {
                                objectOutputStream = new ObjectOutputStream(outputStream);
                                objectOutputStream.writeObject(new BlockIslandMessage(myNick, idIsland));
                            }
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                        }
                        idStudentToMove = -1;
                    }
                }
            });
            iButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if(allI.get(i).getProhibition()){
                JLabel soc = new JLabel();
                ImageIcon socIcon = new ImageIcon(this.getClass().getResource("/Block.png"));
                Image socImage = socIcon.getImage();
                newImg = socImage.getScaledInstance(26,26,Image.SCALE_SMOOTH);
                socIcon = new ImageIcon(newImg);
                soc.setIcon(socIcon);
                Border border = BorderFactory.createLineBorder(Color.BLACK);
                soc.setBorder(border);
                soc.setBounds(4,110,26,26);
                is.add(soc);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.BLUE) > 0){
                JLabel sb = new JLabel();
                ImageIcon sbIcon = new ImageIcon(this.getClass().getResource("/Stud_blue.png"));
                Image sbImage = sbIcon.getImage();
                newImg =sbImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                sbIcon = new ImageIcon(newImg);
                sb.setIcon(sbIcon);
                sb.setOpaque(false);
                sb.setBounds(15, 60, 30, 30);
                is.add(sb);
                JLabel b = new JLabel();
                b.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.BLUE));
                b.setHorizontalAlignment(SwingConstants.CENTER);
                b.setVerticalAlignment(SwingConstants.CENTER);
                b.setFont(new Font("MV Boli", Font.PLAIN, 10));
                b.setBounds(15,90,30,10);
                is.add(b);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.YELLOW) > 0){
                JLabel sy = new JLabel();
                ImageIcon syIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow.png"));
                Image syImage = syIcon.getImage();
                newImg =syImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                syIcon = new ImageIcon(newImg);
                sy.setIcon(syIcon);
                sy.setOpaque(false);;
                sy.setBounds(55, 60, 30, 30);
                is.add(sy);
                JLabel y = new JLabel();
                y.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.YELLOW));
                y.setHorizontalAlignment(SwingConstants.CENTER);
                y.setVerticalAlignment(SwingConstants.CENTER);
                y.setFont(new Font("MV Boli", Font.PLAIN, 10));
                y.setBounds(55,90,30,10);
                is.add(y);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.RED) > 0){
                JLabel sr = new JLabel();
                ImageIcon srIcon = new ImageIcon(this.getClass().getResource("/Stud_red.png"));
                Image srImage = srIcon.getImage();
                newImg =srImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                srIcon = new ImageIcon(newImg);
                sr.setIcon(srIcon);
                sr.setOpaque(false);;
                sr.setBounds(95, 60, 30, 30);
                is.add(sr);
                JLabel r = new JLabel();
                r.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.RED));
                r.setHorizontalAlignment(SwingConstants.CENTER);
                r.setVerticalAlignment(SwingConstants.CENTER);
                r.setFont(new Font("MV Boli", Font.PLAIN, 10));
                r.setBounds(95,90,30,10);
                is.add(r);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.GREEN) > 0){
                JLabel sg = new JLabel();
                ImageIcon sgIcon = new ImageIcon(this.getClass().getResource("/Stud_green.png"));
                Image sgImage = sgIcon.getImage();
                newImg =sgImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                sgIcon = new ImageIcon(newImg);
                sg.setIcon(sgIcon);
                sg.setOpaque(false);;
                sg.setBounds(35, 100, 30, 30);
                is.add(sg);
                JLabel g = new JLabel();
                g.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.GREEN));
                g.setHorizontalAlignment(SwingConstants.CENTER);
                g.setVerticalAlignment(SwingConstants.CENTER);
                g.setFont(new Font("MV Boli", Font.PLAIN, 10));
                g.setBounds(35,130,30,10);
                is.add(g);
            }

            if(allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.PINK) > 0){
                JLabel sp = new JLabel();
                ImageIcon spIcon = new ImageIcon(this.getClass().getResource("/Stud_pink.png"));
                Image spImage = spIcon.getImage();
                newImg =spImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                spIcon = new ImageIcon(newImg);
                sp.setIcon(spIcon);
                sp.setOpaque(false);;
                sp.setBounds(75, 100, 30, 30);
                is.add(sp);
                JLabel p = new JLabel();
                p.setText("x"+allI.get(i).getReport().getColorStudents(it.polimi.ingsw.model.Color.PINK));
                p.setHorizontalAlignment(SwingConstants.CENTER);
                p.setVerticalAlignment(SwingConstants.CENTER);
                p.setFont(new Font("MV Boli", Font.PLAIN, 10));
                p.setBounds(75,130,30,10);
                is.add(p);
            }

            //Show Mother Nature:
            if(report.getMT()==i){
                JLabel mt = new JLabel();
                ImageIcon mtIcon = new ImageIcon(this.getClass().getResource("/MT.png"));
                posMT = i;
                Image mtImage = mtIcon.getImage();
                newImg =mtImage.getScaledInstance(35, 45,  Image.SCALE_SMOOTH);
                mtIcon = new ImageIcon(newImg);
                mt.setIcon(mtIcon);
                mt.setOpaque(false);;
                mt.setBounds(30, 5, 35, 45);
                is.add(mt);
            }

            //Show tower:
            if(allI.get(i).getReport().getOwner()!=null){
                JLabel to = new JLabel();
                ImageIcon toIcon;
                switch (allI.get(i).getReport().getOwner()){
                    case WHITE -> toIcon = new ImageIcon(this.getClass().getResource("/Tower_white.png"));
                    case BLACK -> toIcon = new ImageIcon(this.getClass().getResource("/Tower_black.png"));
                    default -> toIcon = new ImageIcon(this.getClass().getResource("/Tower_grey.png"));
                }
                Image toImage = toIcon.getImage();
                newImg = toImage.getScaledInstance(35, 45,  Image.SCALE_SMOOTH);
                toIcon = new ImageIcon(newImg);
                to.setIcon(toIcon);
                to.setOpaque(false);
                to.setBounds(75, 5, 35, 45);
                is.add(to);
                JLabel nt = new JLabel();
                nt.setText("x"+allI.get(i).getReport().getTowerNumbers());
                nt.setHorizontalAlignment(SwingConstants.CENTER);
                nt.setVerticalAlignment(SwingConstants.CENTER);
                nt.setFont(new Font("MV Boli", Font.PLAIN, 10));
                nt.setBounds(75,50,35,10);
                is.add(nt);
            }
        }

        //Display clouds:
        ArrayList<ArrayList<Student>> allS = report.getStudentsOnClouds();
        len = allS.size();
        for(int i=0; i<len; i++){
            JLabel cl = new JLabel();
            ImageIcon clIcon;
            if(report.getNumPlayers()==3){
                if(i==0) clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_1.png"));
                else if(i==1) clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_2.png"));
                else clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_3.png"));

                for(int j=0; j<allS.get(i).size(); j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon = new ImageIcon(this.getClass().getResource(allS.get(i).get(j).getSprite()));
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(5, 15, 30, 30);
                        case 1 -> st.setBounds(54, 5, 30, 30);
                        case 2 -> st.setBounds(59, 56, 30, 30);
                        case 3 -> st.setBounds(11, 60, 30, 30);
                    }
                    cl.add(st);
                }
            }
            else{
                if(i==0) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_1.png"));
                else if(i==1) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_2.png"));
                else if(i==2) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_3.png"));
                else clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_4.png"));

                for(int j=0; j<allS.get(i).size(); j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon = new ImageIcon(this.getClass().getResource(allS.get(i).get(j).getSprite()));
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(10, 27, 30, 30);
                        case 1 -> st.setBounds(56, 13, 30, 30);
                        case 2 -> st.setBounds(46, 61, 30, 30);
                    }
                    cl.add(st);
                }
            }
            Image clImage = clIcon.getImage();
            Image newImg = clImage.getScaledInstance(100, 100,  Image.SCALE_SMOOTH);
            clIcon = new ImageIcon(newImg);
            cl.setIcon(clIcon);
            cl.setOpaque(false);
            cl.setBounds(150+105*i, 160, 100, 100);
            containerIslands.add(cl);

            JButton clButton = new JButton();
            clButton.setContentAreaFilled(false);
            clButton.setBorderPainted(false);
            clButton.setBounds(150+105*i, 160, 100, 100);
            containerIslands.add(clButton);

            final int posCloud = i;
            clButton.addActionListener(e->{
                try {
                    synchronized (lockWrite) {
                        objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(new SelectCloudMessage(myNick, posCloud));
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            clButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        containerIslands.repaint();

        //Display info:
        containerInfo.removeAll();
        JLabel name = new JLabel();
        name.setText(report.getTurnOf());
        name.setHorizontalAlignment(SwingConstants.CENTER);
        name.setVerticalAlignment(SwingConstants.CENTER);
        name.setFont(new Font("MV Boli", Font.BOLD, 9));
        name.setBounds(0,0,105,20);
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
        phase.setFont(new Font("MV Boli", Font.BOLD, 9));
        phase.setBounds(0,20,105,20);
        containerInfo.add(phase);
        containerInfo.repaint();

        //Display my dashboard:
        containerDash.removeAll();
        len = report.getMyEntrance().size();
        for(int i=0; i<len; i++){
            JButton st = new JButton();
            ImageIcon stIcon = new ImageIcon(this.getClass().getResource(report.getMyEntrance().get(i).getSprite()));
            Image stImage = stIcon.getImage();
            Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            stIcon = new ImageIcon(newImg);
            st.setIcon(stIcon);
            st.setContentAreaFilled(false);
            st.setBorderPainted(false);
            st.setBounds(10+30*((i+1)%2),20+31*((i+1)/2),  30, 30);
            containerDash.add(st);

            final int idStudent = report.getMyEntrance().get(i).getId();
            final it.polimi.ingsw.model.Color colorStudent = report.getMyEntrance().get(i).getColor();
            st.addActionListener(e-> {
                if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==7 && idStudentToMove!=-1){
                    try {
                        synchronized (lockWrite) {
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new ExchangeStudentMessage(myNick, idStudentToMove, idStudent, departureType, departureID, Location.ENTRANCE, -1));
                        }
                        }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                    idStudentToMove = -1;
                }
                else{
                    idStudentToMove = idStudent;
                    colorStudentToMove = colorStudent;
                    departureType = Location.ENTRANCE;
                    departureID = -1;
                }
            });
            st.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        for(int i=0; i<5; i++){
            len = report.getMyCanteen().get(i);
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

            JButton taButton = new JButton();
            taButton.setContentAreaFilled(false);
            taButton.setBorderPainted(false);
            taButton.setBounds(95, 22+32*i, 24*10, 23);
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
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new BlockColorMessage(myNick, colorTable));
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==10 && report.getMyCanteen().get(pos)>0){
                    try {
                        synchronized (lockWrite) {
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new ExchangeStudentMessage(myNick, idStudentToMove, report.getLastMyCanteen().get(pos), Location.ENTRANCE, -1, Location.CANTEEN, -1));
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(report.getActiveCard()>-1 && report.getChar().get(report.getActiveCard()).getId()==12){
                    try {
                        synchronized (lockWrite) {
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new PutBackMessage(myNick, colorTable));
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(colorStudentToMove==colorTable){
                    try {
                        synchronized (lockWrite) {
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(new MoveStudentMessage(myNick, idStudentToMove, departureType, departureID, Location.CANTEEN, -1));
                        }
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                    }
                    colorStudentToMove = null;
                }
            });
            taButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            if(report.getProfessors().get(colInDashC[i]).equals(report.getNamePlayer())){
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
        numC.setText("x"+report.getMyCoins());

        //Show played card:
        if(report.getMyLastCard() != null){
            containerLastCard.removeAll();
            JLabel c = new JLabel();
            ImageIcon cIcon = new ImageIcon(this.getClass().getResource(report.getMyLastCard().getFront()));
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(85, 125,  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            c.setIcon(cIcon);
            c.setBounds(0, 0, 85, 125);
            containerLastCard.add(c);
            containerLastCard.repaint();
        }

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

            if(report.getChar().get(i).getTypeCharacter() == CharacterType.MOVEMENT || report.getChar().get(i).getTypeCharacter() == CharacterType.EXCHANGE){
                MovementCharacter mc = (MovementCharacter)report.getChar().get(i);
                ArrayList<Student> studs = mc.getStudents();
                for(int j=0; j<studs.size(); j++){
                    JButton soc = new JButton();
                    ImageIcon socIcon = new ImageIcon(this.getClass().getResource(studs.get(j).getSprite()));
                    Image socImage = socIcon.getImage();
                    Image newImg = socImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                    socIcon = new ImageIcon(newImg);
                    soc.setIcon(socIcon);
                    soc.setContentAreaFilled(false);
                    soc.setBorderPainted(false);
                    soc.setBounds(22+j%2*30,(j/2)*30+55,30, 30);
                    containerCharacters.get(i).add(soc);

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
                    });
                    soc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            else if(report.getChar().get(i).getTypeCharacter() == CharacterType.ACTION){
                ActionCharacter ac = (ActionCharacter) report.getChar().get(i);
                for(int j=0; j<ac.getNumTokens(); j++){
                    JLabel soc = new JLabel();
                    ImageIcon socIcon = new ImageIcon(this.getClass().getResource("/Block.png"));
                    Image socImage = socIcon.getImage();
                    Image newImg = socImage.getScaledInstance(26,26,Image.SCALE_SMOOTH);
                    socIcon = new ImageIcon(newImg);
                    soc.setIcon(socIcon);
                    Border border = BorderFactory.createLineBorder(Color.BLACK);
                    soc.setBorder(border);
                    soc.setBounds(22+j%2*30,(j/2)*30+70,26,26);
                    containerCharacters.get(i).add(soc);
                }
            }

            //Add coins if selected:
            int addedValue = report.getChar().get(i).getCost() - report.getChar().get(i).getOriginalCost();
            for(int j=0; j<addedValue; j++){
                JLabel mo = new JLabel();
                ImageIcon moIcon = new ImageIcon(this.getClass().getResource("/AddCoin.png"));
                Image moImage = moIcon.getImage();
                Image newImg = moImage.getScaledInstance(25, 15,  Image.SCALE_SMOOTH);
                moIcon = new ImageIcon(newImg);
                mo.setIcon(moIcon);
                mo.setOpaque(false);
                mo.setBounds(5, 18+j*10, 25, 15);
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
    }

    public void hideGUI(){
            window.setVisible(false);
    }
}