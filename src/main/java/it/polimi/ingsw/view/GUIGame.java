package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Student;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GUIGame {
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private JFrame window = new JFrame("Eriantys"); //Main window
    private JLabel bgLabel; //Main background
    private JLabel myCards = new JLabel(); //My cards'container



    public GUIGame(Socket socket, GameReport report) throws IOException {
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        createGUI(report);
        displayReport(report);
    }



    public void openGUI(){
        window.setVisible(true);
    }



    private void createGUI(GameReport report){
        //Background image:
        ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("/Game_bg.jpg"));
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(900, 650,  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(900,650);

        //Main window:
        window.add(bgLabel);
        window.setSize(900, 685);
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //Show players'dashboards:
        ImageIcon dashIcon = new ImageIcon(this.getClass().getResource("/Dashboard.png"));
        Image dashImage = dashIcon.getImage();
        newImg = dashImage.getScaledInstance(500, 200,  Image.SCALE_SMOOTH);
        dashIcon = new ImageIcon(newImg);
        JLabel dashLabel = new JLabel(dashIcon);
        dashLabel.setBounds(200, 410, 500, 200);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        dashLabel.setBorder(border);
        bgLabel.add(dashLabel);

        //My cards'container:
        myCards.setBounds(-2, 610, 900, 45);
        myCards.setOpaque(true);
        Color colorLabel = new Color(128,128,128,175);
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

        //Opponents'container:
        JLabel opponents = new JLabel();
        opponents.setBounds(790, 100, 100, 24+report.getNumPlayers()*22);
        opponents.setOpaque(true);
        opponents.setBackground(colorLabel);
        opponents.setBorder(border);
        bgLabel.add(opponents);

        JLabel titleO = new JLabel();
        titleO.setText("All players:");
        titleO.setHorizontalAlignment(SwingConstants.CENTER);
        titleO.setVerticalAlignment(SwingConstants.CENTER);
        titleO.setFont(new Font("MV Boli", Font.BOLD, 13));
        titleO.setBounds(2,0,100,20);
        opponents.add(titleO);

        for(int i=0; i<report.getNumPlayers(); i++){
            JButton od = new JButton(report.getOpponentsNick().get(i));
            if(report.getOpponentsNick().get(i).equals(report.getNamePlayer())) od.setEnabled(false);
            od.setBounds(2, 22+22*i, 96, 20);
            opponents.add(od);
        }
    }



    private void displayReport(GameReport report){

        //Display my cards:
        ArrayList<Card> myC = report.getMyCards();
        int len = myC.size();
        for(int i=0; i<len; i++){
            JButton bc = new JButton();
            ImageIcon cIcon = new ImageIcon(this.getClass().getResource(myC.get(i).getFront()));
            Image cImage = cIcon.getImage();
            Image newImg = cImage.getScaledInstance(75, 125,  Image.SCALE_SMOOTH);
            cIcon = new ImageIcon(newImg);
            bc.setIcon(cIcon);
            bc.setBounds(90+80*i, 2, 75, 125);
            myCards.add(bc);
        }

        //Display all islands:
        ArrayList<ArrayList<Integer>> allI = report.getStudentsOnIslands();
        len = allI.size();
        for(int i=0; i<len; i++){

            JLabel is = new JLabel();
            int rand = 1+(int)(Math.random()*3);
            ImageIcon isIcon;
            switch (rand){
                case 1 -> isIcon = new ImageIcon(this.getClass().getResource("/Island_1.png"));
                case 2 -> isIcon = new ImageIcon(this.getClass().getResource("/Island_2.png"));
                default -> isIcon = new ImageIcon(this.getClass().getResource("/Island_3.png"));
            }
            Image isImage = isIcon.getImage();
            Image newImg = isImage.getScaledInstance(140, 140,  Image.SCALE_SMOOTH);
            isIcon = new ImageIcon(newImg);
            is.setIcon(isIcon);
            is.setOpaque(false);
            if(i<5) is.setBounds(5+140*i, 5, 140, 140);
            else if(i<7) is.setBounds(565, 145+140*(i-5), 140, 140);
            else if(i<11) is.setBounds(425-140*(i-7), 285, 140, 140);
            else is.setBounds(5, 145, 140, 140);
            bgLabel.add(is);

            JButton sb = new JButton();
            ImageIcon sbIcon;
            if(allI.get(i).get(0)==0) sbIcon = new ImageIcon(this.getClass().getResource("/Stud_blue_T.png"));
            else sbIcon = new ImageIcon(this.getClass().getResource("/Stud_blue.png"));
            Image sbImage = sbIcon.getImage();
            newImg =sbImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            sbIcon = new ImageIcon(newImg);
            sb.setIcon(sbIcon);
            sb.setOpaque(false);
            sb.setContentAreaFilled(false);
            sb.setBorderPainted(false);
            sb.setFocusPainted(false);
            sb.setBounds(15, 60, 30, 30);
            is.add(sb);
            JLabel b = new JLabel();
            b.setText("x"+allI.get(i).get(0));
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setVerticalAlignment(SwingConstants.CENTER);
            b.setFont(new Font("MV Boli", Font.PLAIN, 10));
            b.setBounds(15,90,30,10);
            is.add(b);

            JButton sy = new JButton();
            ImageIcon syIcon;
            if(allI.get(i).get(1)==0) syIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow_T.png"));
            else syIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow.png"));
            Image syImage = syIcon.getImage();
            newImg =syImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            syIcon = new ImageIcon(newImg);
            sy.setIcon(syIcon);
            sy.setOpaque(false);
            sy.setContentAreaFilled(false);
            sy.setBorderPainted(false);
            sy.setFocusPainted(false);
            sy.setBounds(55, 60, 30, 30);
            is.add(sy);
            JLabel y = new JLabel();
            y.setText("x"+allI.get(i).get(1));
            y.setHorizontalAlignment(SwingConstants.CENTER);
            y.setVerticalAlignment(SwingConstants.CENTER);
            y.setFont(new Font("MV Boli", Font.PLAIN, 10));
            y.setBounds(55,90,30,10);
            is.add(y);

            JButton sr = new JButton();
            ImageIcon srIcon;
            if(allI.get(i).get(2)==0) srIcon = new ImageIcon(this.getClass().getResource("/Stud_red_T.png"));
            else srIcon = new ImageIcon(this.getClass().getResource("/Stud_red.png"));
            Image srImage = srIcon.getImage();
            newImg =srImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            srIcon = new ImageIcon(newImg);
            sr.setIcon(srIcon);
            sr.setOpaque(false);
            sr.setContentAreaFilled(false);
            sr.setBorderPainted(false);
            sr.setFocusPainted(false);
            sr.setBounds(95, 60, 30, 30);
            is.add(sr);
            JLabel r = new JLabel();
            r.setText("x"+allI.get(i).get(2));
            r.setHorizontalAlignment(SwingConstants.CENTER);
            r.setVerticalAlignment(SwingConstants.CENTER);
            r.setFont(new Font("MV Boli", Font.PLAIN, 10));
            r.setBounds(95,90,30,10);
            is.add(r);

            JButton sg = new JButton();
            ImageIcon sgIcon;
            if(allI.get(i).get(3)==0) sgIcon = new ImageIcon(this.getClass().getResource("/Stud_green_T.png"));
            else sgIcon = new ImageIcon(this.getClass().getResource("/Stud_green.png"));
            Image sgImage = sgIcon.getImage();
            newImg =sgImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            sgIcon = new ImageIcon(newImg);
            sg.setIcon(sgIcon);
            sg.setOpaque(false);
            sg.setContentAreaFilled(false);
            sg.setBorderPainted(false);
            sg.setFocusPainted(false);
            sg.setBounds(35, 100, 30, 30);
            is.add(sg);
            JLabel g = new JLabel();
            g.setText("x"+allI.get(i).get(3));
            g.setHorizontalAlignment(SwingConstants.CENTER);
            g.setVerticalAlignment(SwingConstants.CENTER);
            g.setFont(new Font("MV Boli", Font.PLAIN, 10));
            g.setBounds(35,130,30,10);
            is.add(g);

            JButton sp = new JButton();
            ImageIcon spIcon;
            if(allI.get(i).get(4)==0) spIcon = new ImageIcon(this.getClass().getResource("/Stud_pink_T.png"));
            else spIcon = new ImageIcon(this.getClass().getResource("/Stud_pink.png"));
            Image spImage = spIcon.getImage();
            newImg =spImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
            spIcon = new ImageIcon(newImg);
            sp.setIcon(spIcon);
            sp.setOpaque(false);
            sp.setContentAreaFilled(false);
            sp.setBorderPainted(false);
            sp.setFocusPainted(false);
            sp.setBounds(75, 100, 30, 30);
            is.add(sp);
            JLabel p = new JLabel();
            p.setText("x"+allI.get(i).get(4));
            p.setHorizontalAlignment(SwingConstants.CENTER);
            p.setVerticalAlignment(SwingConstants.CENTER);
            p.setFont(new Font("MV Boli", Font.PLAIN, 10));
            p.setBounds(75,130,30,10);
            is.add(p);

            //Show Mother Nature:
            JButton mt = new JButton();
            ImageIcon mtIcon;
            if(report.getMT()==i) mtIcon = new ImageIcon(this.getClass().getResource("/MT.png"));
            else mtIcon = new ImageIcon(this.getClass().getResource("/MT_T.png"));
            Image mtImage = mtIcon.getImage();
            newImg =mtImage.getScaledInstance(35, 45,  Image.SCALE_SMOOTH);
            mtIcon = new ImageIcon(newImg);
            mt.setIcon(mtIcon);
            mt.setOpaque(false);
            mt.setContentAreaFilled(false);
            mt.setBorderPainted(false);
            mt.setFocusPainted(false);
            mt.setBounds(30, 10, 35, 45);
            is.add(mt);
        }

        //Display clouds:
        ArrayList<ArrayList<Student>> allS = report.getStudentsOnClouds();
        len = allS.size();
        for(int i=0; i<len; i++){
            JButton cl = new JButton();
            ImageIcon clIcon;
            if(report.getNumPlayers()==3){
                if(i==0) clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_1.png"));
                else if(i==1) clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_2.png"));
                else clIcon = new ImageIcon(this.getClass().getResource("/CloudX4_3.png"));

                for(int j=0; j<4; j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon;
                    switch (allS.get(i).get(j).getColor()){
                        case BLUE -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_blue.png"));
                        case YELLOW -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow.png"));
                        case RED -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_red.png"));
                        case GREEN -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_green.png"));
                        default -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_pink.png"));
                    }
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(5, 15, 30, 30);
                        case 1 -> st.setBounds(60, 5, 30, 30);
                        case 2 -> st.setBounds(60, 60, 30, 30);
                        case 3 -> st.setBounds(10, 60, 30, 30);
                    }
                    cl.add(st);
                }
            }
            else{
                if(i==0) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_1.png"));
                else if(i==1) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_2.png"));
                else if(i==2) clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_3.png"));
                else clIcon = new ImageIcon(this.getClass().getResource("/CloudX3_4.png"));

                for(int j=0; j<3; j++){
                    JLabel st = new JLabel();
                    ImageIcon stIcon;
                    switch (allS.get(i).get(j).getColor()){
                        case BLUE -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_blue.png"));
                        case YELLOW -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_yellow.png"));
                        case RED -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_red.png"));
                        case GREEN -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_green.png"));
                        default -> stIcon = new ImageIcon(this.getClass().getResource("/Stud_pink.png"));
                    }
                    Image stImage = stIcon.getImage();
                    Image newImg = stImage.getScaledInstance(30, 30,  Image.SCALE_SMOOTH);
                    stIcon = new ImageIcon(newImg);
                    st.setIcon(stIcon);
                    st.setOpaque(false);
                    switch(j){
                        case 0 -> st.setBounds(5, 20, 30, 30);
                        case 1 -> st.setBounds(60, 10, 30, 30);
                        case 2 -> st.setBounds(60, 50, 30, 30);
                    }
                    cl.add(st);
                }
            }
            Image clImage = clIcon.getImage();
            Image newImg = clImage.getScaledInstance(100, 100,  Image.SCALE_SMOOTH);
            clIcon = new ImageIcon(newImg);
            cl.setIcon(clIcon);
            cl.setOpaque(false);
            cl.setContentAreaFilled(false);
            cl.setBorderPainted(false);
            cl.setFocusPainted(false);
            cl.setBounds(155+105*i, 165, 100, 100);
            bgLabel.add(cl);
        }
    }
}