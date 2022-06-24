package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.messages.AddMeMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.view.GameReport;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

/**
 * This class is the object that represent the window that permit the player to join a game
 */
public class GUIEntry{

    /**
     * This class represent a transparent panel to cover the GUI while waiting for an update from the server
     */
    private class TransparentPanel extends JPanel {

        /**
         * This method is the constructor of the class
         */
        public TransparentPanel(){
            setOpaque(false);
        }

        /**
         * This method build the panel
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g) {
            g.setColor(getBackground());
            Rectangle r = g.getClipBounds();
            g.fillRect(r.x, r.y, r.width, r.height);
            super.paintComponent(g);
        }
    }

    private final InputStream inputStream;
    private final OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private final JFrame window = new JFrame("Eriantys"); //Main window
    private JTextField textField;
    private JSlider slider;
    private final JButton playButton = new JButton(); //To join a match
    private GameReport report;
    private boolean requestSent = false; //To avoid a client joins more than one match at the same time
    private JLabel connDesc, uncDesc; //To display the status of the research
    private JLabel connLabel, uncLabel; //To display the status of the research
    private String nickname;

    private final Object lockWrite;

    private final JLayeredPane layered = new JLayeredPane();
    private final JPanel loading = new TransparentPanel();

    /**
     * This method is the constructor of the game
     * @param socket is the socket of the connection with the server
     * @param lockWrite is a lock to synchronize the object
     * @throws IOException when there is an input/output error
     */
    public GUIEntry(Socket socket, Object lockWrite) throws IOException{
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();

        this.lockWrite = lockWrite;

        createGUI();
    }

    /**
     * This method is called to open the gui to join a new game
     * @return a GameReport object to pass to GUIGame to play the game
     */
    public GameReport openGUI(){
        window.setVisible(true);

        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            report = (GameReport) objectInputStream.readObject();
        }catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        while(report.getError() != null && report.getError().equals("This nickname is already taken")){
            connDesc.setVisible(false);
            connLabel.setVisible(false);
            uncDesc.setVisible(true);
            uncLabel.setVisible(true);
            requestSent = false;
            JOptionPane.showMessageDialog(null, "<html>This nickname was already taken!<br/>Please choose another one...</html>","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            try {
                objectInputStream = new ObjectInputStream(inputStream);
                report = (GameReport) objectInputStream.readObject();
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
        while(report.getTurnOf() == null){
            try {
                objectInputStream = new ObjectInputStream(inputStream);
                report = (GameReport) objectInputStream.readObject();
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(),"Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }

        layered.setLayer(loading, 10);
        layered.repaint();
        return report;
    }

    /**
     * This method is called to hide the GUIEntry window
     */
    public void hideGUI(){
        window.setVisible(false);
        window.dispose();
    }


    private void createGUI(){

        //Background image:
        ImageIcon bgIcon = ScalingUtils.getImage("/Lobby_bg.png");
        Image bgImage = bgIcon.getImage();
        Image newImg = bgImage.getScaledInstance(ScalingUtils.scaleX(700), ScalingUtils.scaleY(400),  Image.SCALE_SMOOTH);
        bgIcon = new ImageIcon(newImg);
        JLabel bgLabel = new JLabel(bgIcon);
        bgLabel.setSize(ScalingUtils.scaleX(700), ScalingUtils.scaleY(400));

        layered.add(bgLabel, Integer.valueOf(5));
        layered.setSize(ScalingUtils.scaleX(700), ScalingUtils.scaleY(400));
        layered.setLayout(null);

        //Main window:
        window.add(layered);
        window.setSize(ScalingUtils.scaleX(700), ScalingUtils.scaleY(435));
        window.setResizable(false);
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        //Show help Buttons:
        JButton creditButton = new JButton();
        ImageIcon creditIcon = ScalingUtils.getImage("/Credits.png");
        Image creditImage = creditIcon.getImage();
        newImg = creditImage.getScaledInstance(ScalingUtils.scaleX(40, 700), ScalingUtils.scaleY(40, 400),  Image.SCALE_SMOOTH);
        creditIcon = new ImageIcon(newImg);
        creditButton.setIcon(creditIcon);
        creditButton.setContentAreaFilled(false);
        creditButton.setBorderPainted(false);
        creditButton.setBounds(ScalingUtils.scaleX(10, 700), ScalingUtils.scaleY(10, 400), ScalingUtils.scaleX(40, 700), ScalingUtils.scaleY(40, 400));
        creditButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(creditButton);

        JLabel halo = new JLabel();
        ImageIcon haloIcon = ScalingUtils.getImage("/Halo_light.png");
        Image haloImage = haloIcon.getImage();
        newImg =haloImage.getScaledInstance(ScalingUtils.scaleX(50, 700), ScalingUtils.scaleY(50, 400),  Image.SCALE_SMOOTH);
        haloIcon = new ImageIcon(newImg);
        halo.setIcon(haloIcon);
        halo.setOpaque(false);
        halo.setBounds(ScalingUtils.scaleX(5, 700), ScalingUtils.scaleY(5, 400), ScalingUtils.scaleX(50, 700), ScalingUtils.scaleY(50, 400));
        halo.setVisible(false);
        bgLabel.add(halo);

        CreditsDialog credits = new CreditsDialog(window);
        creditButton.addActionListener(e->{
            credits.showDialog();
        });
        creditButton.addMouseListener(new MouseAdapter() {
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

        JButton ruleButton = new JButton();
        ImageIcon ruleIcon = ScalingUtils.getImage("/Book.png");
        Image ruleImage = ruleIcon.getImage();
        newImg = ruleImage.getScaledInstance(ScalingUtils.scaleX(40, 700), ScalingUtils.scaleY(40, 400),  Image.SCALE_SMOOTH);
        ruleIcon = new ImageIcon(newImg);
        ruleButton.setIcon(ruleIcon);
        ruleButton.setContentAreaFilled(false);
        ruleButton.setBorderPainted(false);
        ruleButton.setBounds(ScalingUtils.scaleX(10, 700), ScalingUtils.scaleY(60, 400), ScalingUtils.scaleX(40, 700), ScalingUtils.scaleY(40, 400));
        ruleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bgLabel.add(ruleButton);

        JLabel halo2 = new JLabel();
        ImageIcon haloIcon2 = ScalingUtils.getImage("/Halo_red.png");
        Image haloImage2 = haloIcon2.getImage();
        newImg = haloImage2.getScaledInstance(ScalingUtils.scaleX(50, 700), ScalingUtils.scaleY(50, 400),  Image.SCALE_SMOOTH);
        haloIcon2 = new ImageIcon(newImg);
        halo2.setIcon(haloIcon2);
        halo2.setOpaque(false);
        halo2.setBounds(ScalingUtils.scaleX(5, 700), ScalingUtils.scaleY(55, 400), ScalingUtils.scaleX(50, 700), ScalingUtils.scaleY(50, 400));
        halo2.setVisible(false);
        bgLabel.add(halo2);

        RulesDialog rulesDialog = new RulesDialog(window);
        ruleButton.addActionListener(e->{
            rulesDialog.showDialog();
        });
        ruleButton.addMouseListener(new MouseAdapter() {
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

        //Label containing the setting menu:
        JPanel panel = new TransparentPanel();
        panel.setLayout(null);
        panel.setBounds(ScalingUtils.scaleX(75, 700), ScalingUtils.scaleY(200, 400), ScalingUtils.scaleX(550, 700), ScalingUtils.scaleY(155, 400));
        //panel.setOpaque(true);
        Color colorLabel = new Color(190, 190, 190,210);
        panel.setBackground(colorLabel);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(border);
        bgLabel.add(panel);

        //Title of the label:
        JLabel title = new JLabel();
        title.setText("Find your next match, now!!!");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(18)));
        title.setBounds(ScalingUtils.scaleX(125, 500), ScalingUtils.scaleY(5, 155), ScalingUtils.scaleX(300, 500), ScalingUtils.scaleY(20, 155));
        panel.add(title);

        //Text to state the requirements for the nickname:
        JLabel nick = new JLabel();
        nick.setText("Enter your nickname");
        nick.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(14)));
        nick.setHorizontalAlignment(SwingConstants.CENTER);
        nick.setVerticalAlignment(SwingConstants.CENTER);
        nick.setBounds(ScalingUtils.scaleX(25, 500), ScalingUtils.scaleY(30, 155), ScalingUtils.scaleX(145, 500), ScalingUtils.scaleY(20, 155));
        panel.add(nick);
        JLabel req = new JLabel();
        req.setText("(It must be different from other players' ones)");
        req.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        req.setHorizontalAlignment(SwingConstants.CENTER);
        req.setVerticalAlignment(SwingConstants.CENTER);
        req.setBounds(ScalingUtils.scaleX(170, 500), ScalingUtils.scaleY(30, 155), ScalingUtils.scaleX(230, 500), ScalingUtils.scaleY(20, 155));
        panel.add(req);

        //TextField to get player's nickname
        textField = new JTextField(16);
        textField.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        textField.setBounds(ScalingUtils.scaleX(400, 500), ScalingUtils.scaleY(30, 155), ScalingUtils.scaleX(125, 500), ScalingUtils.scaleY(20, 155));
        panel.add(textField);

        //Text to insert number of players:
        JLabel pla = new JLabel();
        pla.setText("Set number of players");
        pla.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(13)));
        pla.setHorizontalAlignment(SwingConstants.CENTER);
        pla.setVerticalAlignment(SwingConstants.CENTER);
        pla.setBounds(ScalingUtils.scaleX(40, 500), ScalingUtils.scaleY(55, 155), ScalingUtils.scaleX(125, 500), ScalingUtils.scaleY(30, 155));
        panel.add(pla);

        //Slider to select the number of players:
        slider = new JSlider(JSlider.HORIZONTAL,2,4,2);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setOpaque(false);
        slider.setBounds(ScalingUtils.scaleX(165, 500), ScalingUtils.scaleY(55, 155), ScalingUtils.scaleX(75, 500), ScalingUtils.scaleY(30, 155));
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(slider);

        //Text to choose the rule type:
        JLabel rul = new JLabel();
        rul.setText("Set of rules:");
        rul.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(14)));
        rul.setHorizontalAlignment(SwingConstants.CENTER);
        rul.setVerticalAlignment(SwingConstants.CENTER);
        rul.setBounds(ScalingUtils.scaleX(275, 500), ScalingUtils.scaleY(55, 155), ScalingUtils.scaleX(95, 500), ScalingUtils.scaleY(30, 155));
        panel.add(rul);
        JRadioButton option1 = new JRadioButton("Simple",true);
        option1.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(14)));
        option1.setOpaque(false);
        option1.setFocusPainted(false);
        option1.setBounds(ScalingUtils.scaleX(370, 500), ScalingUtils.scaleY(55, 155), ScalingUtils.scaleX(65, 500), ScalingUtils.scaleY(30, 155));
        panel.add(option1);
        JRadioButton option2 = new JRadioButton("Complete",false);
        option2.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(14)));
        option2.setOpaque(false);
        option2.setFocusPainted(false);
        option2.setBounds(ScalingUtils.scaleX(435, 500), ScalingUtils.scaleY(55, 155), ScalingUtils.scaleX(100, 500), ScalingUtils.scaleY(30, 155));
        panel.add(option2);
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        option1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //Play button:
        ImageIcon playIcon = ScalingUtils.getImage("/Play.png");
        Image playImage = playIcon.getImage();
        newImg = playImage.getScaledInstance(ScalingUtils.scaleX(50, 500), ScalingUtils.scaleY(50, 155),  Image.SCALE_SMOOTH);
        playIcon = new ImageIcon(newImg);
        playButton.setIcon(playIcon);
        playButton.setBounds(ScalingUtils.scaleX(245, 500), ScalingUtils.scaleY(90, 155), ScalingUtils.scaleX(50, 500), ScalingUtils.scaleY(50, 155));
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.addActionListener(e->{
            if(requestSent){
                JOptionPane.showMessageDialog(null, "<html>You are waiting to join a match!<br/>Please hold on for a little bit...</html>","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                if(textField.getText() == null || textField.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "<html>You have to choose a nickname</html>","Eriantys - Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    nickname = textField.getText();
                    requestSent = true;
                    Message message = new AddMeMessage(nickname, option2.isSelected(), slider.getValue());
                    try {
                        synchronized (lockWrite) {
                            objectOutputStream = new ObjectOutputStream(outputStream);
                            objectOutputStream.writeObject(message);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    uncDesc.setVisible(false);
                    uncLabel.setVisible(false);
                    connDesc.setVisible(true);
                    connLabel.setVisible(true);
                }
            }
        });
        panel.add(playButton);

        //Status bar:
        JPanel status = new JPanel();
        status.setLayout(null);
        status.setBounds(ScalingUtils.scaleX(308, 500), ScalingUtils.scaleY(126, 155), ScalingUtils.scaleX(212, 500), ScalingUtils.scaleY(24, 155));
        status.setOpaque(false);
        status.setBackground(colorLabel);
        status.setBorder(border);
        panel.add(status);
        JLabel sta = new JLabel();
        sta.setText("Status");
        sta.setFont(new Font("Times New Roman", Font.BOLD, ScalingUtils.scaleFont(12)));
        sta.setHorizontalAlignment(SwingConstants.CENTER);
        sta.setVerticalAlignment(SwingConstants.CENTER);
        sta.setBounds(ScalingUtils.scaleX(2, 212), ScalingUtils.scaleY(4, 24), ScalingUtils.scaleX(40, 212), ScalingUtils.scaleY(16, 24));
        status.add(sta);
        uncDesc = new JLabel();
        uncDesc.setText("Not playing: find a game!");
        uncDesc.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(12)));
        uncDesc.setHorizontalAlignment(SwingConstants.CENTER);
        uncDesc.setVerticalAlignment(SwingConstants.CENTER);
        uncDesc.setBounds(ScalingUtils.scaleX(42, 212), ScalingUtils.scaleY(4, 24), ScalingUtils.scaleX(146, 212), ScalingUtils.scaleY(16, 24));
        status.add(uncDesc);
        connDesc = new JLabel();
        connDesc.setText("Match created: wait for other players...");
        connDesc.setFont(new Font("Times New Roman", Font.PLAIN, ScalingUtils.scaleFont(9)));
        connDesc.setHorizontalAlignment(SwingConstants.CENTER);
        connDesc.setVerticalAlignment(SwingConstants.CENTER);
        connDesc.setBounds(ScalingUtils.scaleX(42, 212), ScalingUtils.scaleY(4, 24), ScalingUtils.scaleX(146, 212), ScalingUtils.scaleY(16, 24));
        connDesc.setVisible(false);
        status.add(connDesc);
        ImageIcon connIcon = ScalingUtils.getImage("/Connected.png");
        Image connImage = connIcon.getImage();
        newImg = connImage.getScaledInstance(ScalingUtils.scaleX(20, 212), ScalingUtils.scaleY(20, 24),  Image.SCALE_SMOOTH);
        connIcon = new ImageIcon(newImg);
        connLabel = new JLabel(connIcon);
        connLabel.setBounds(ScalingUtils.scaleX(190, 212), ScalingUtils.scaleY(2, 24), ScalingUtils.scaleX(20, 212), ScalingUtils.scaleY(20, 24));
        connLabel.setVisible(false);
        status.add(connLabel);
        ImageIcon uncIcon = ScalingUtils.getImage("/Unconnected.png");
        Image uncImage = uncIcon.getImage();
        newImg = uncImage.getScaledInstance(ScalingUtils.scaleX(20, 212), ScalingUtils.scaleY(20, 24),  Image.SCALE_SMOOTH);
        uncIcon = new ImageIcon(newImg);
        uncLabel = new JLabel(uncIcon);
        uncLabel.setBounds(ScalingUtils.scaleX(190, 212), ScalingUtils.scaleY(2, 24), ScalingUtils.scaleX(20, 212), ScalingUtils.scaleY(20, 24));
        status.add(uncLabel);

        //Loading Panel
        loading.setLayout(null);
        loading.setBackground(new Color(210, 210, 210, 200));
        loading.setSize(ScalingUtils.scaleX(700), ScalingUtils.scaleY(400));
        JLabel wait = new JLabel();
        wait.setFont(new Font("MV Boli", Font.BOLD, ScalingUtils.scaleFont(20)));
        wait.setBounds(0, 0, ScalingUtils.scaleX(700, 700), ScalingUtils.scaleY(400, 400));
        wait.setText("<html><body style='text-align: center'>A match has been found!<br/>Please wait...</body></html>");
        wait.setVerticalAlignment(SwingConstants.CENTER);
        wait.setHorizontalAlignment(SwingConstants.CENTER);
        loading.add(wait);
        layered.add(loading, Integer.valueOf(1));

    }
}