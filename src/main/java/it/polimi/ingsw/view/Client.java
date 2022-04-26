package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {

    private String ip;
    private int port;
    private Scanner stdin;

    private Message message;
    private String nickname;
    private String action;
    private String rule, lo;
    private int numPlayers, priority, studentId, arrivalId, departureId, MNmovement, position;
    private Location arrivalType, departureType;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        Scanner socketIn = new Scanner(socket.getInputStream());
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream;
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = null;
        Game game;

        stdin = new Scanner(System.in);
        String socketLine;
        try{
            socketLine = socketIn.nextLine();
            System.out.println(socketLine);

            message = getInputStart();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
            do {
                objectInputStream = new ObjectInputStream(inputStream);
                game = (Game) objectInputStream.readObject();
                showBoard(game);
            }while(game.getCurrentPlayer()==null || !game.getCurrentPlayer().equals(nickname));

            while (true){
                message = getInput();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(message);
                do {
                    objectInputStream = new ObjectInputStream(inputStream);
                    game = (Game) objectInputStream.readObject();
                    showBoard(game);
                }while(game.getCurrentPlayer()==null || !game.getCurrentPlayer().equals(nickname));
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            objectOutputStream.close();
            socket.close();
        }
    }

    private Message getInputStart(){
        boolean completeRules;
        System.out.println("Insert your nickname:");
        nickname = stdin.nextLine();
        do {
            System.out.println("How many players do you want to play with? (a number between 2 and 4)");
            numPlayers = stdin.nextInt();
            stdin.nextLine();
        }while(numPlayers<2 || numPlayers>4);
        System.out.println("Do you want to play by simple (type 'SIMPLE') or complete rules (type 'COMPLETE')?");
        rule = stdin.nextLine();
        if(rule.equals("COMPLETE")) completeRules=true;
        else{
            completeRules=false;
        }
        return new AddMeMessage(nickname, Action.ADDME, completeRules, numPlayers);

    }



    private Message getInput(){
        System.out.println("Select one of the following options:");
        System.out.println("\t-'PLAYCARD' to play an assistant-card");
        System.out.println("\t-'MOVESTUDENT' to move a student from your ENTRANCE to your CANTEEN or an ISLAND");
        System.out.println("\t-'MOVEMOTHERNATURE' to move Mother Nature");
        System.out.println("\t-'SELECTCLOUD' to refill your ENTRANCE with students from a cloud");
        action = stdin.nextLine();

        if(action.equals("PLAYCARD")){
            System.out.println("Choose which card do you want to play (priority):");
            priority = stdin.nextInt();
            stdin.nextLine();
            return new PlayCardMessage(nickname, Action.PLAYCARD, priority);
        }

        else if(action.equals("MOVESTUDENT")){
            System.out.println("Which student do you want to move? (studentId)");
            studentId = stdin.nextInt();
            stdin.nextLine();
            System.out.println("Where the student is? (type ENTRANCE, CANTEEN or ISLAND)");
            lo = stdin.nextLine();
            if(lo.equals("ISLAND")){
                departureType=Location.ISLAND;
                System.out.println("Choose island: (islandId)");
                departureId = stdin.nextInt();
                stdin.nextLine();
            }
            else if(lo.equals("CANTEEN")){
                departureType=Location.CANTEEN;
                departureId = -1;
            }
            else if(lo.equals("ENTRANCE")) {
                departureType=Location.ENTRANCE;
                departureId = -1;
            }
            else{
                departureType = null;
                departureId = -1;
            }
            System.out.println("Where do you want to move the student? (type ENTRANCE, CANTEEN or ISLAND)");
            lo = stdin.nextLine();
            if(lo.equals("ISLAND")){
                arrivalType=Location.ISLAND;
                System.out.println("Choose island: (islandId)");
                arrivalId = stdin.nextInt();
                stdin.nextLine();
            }
            else if(lo.equals("CANTEEN")){
                arrivalType=Location.CANTEEN;
                arrivalId = -1;
            }
            else if(lo.equals("ENTRANCE")) {
                arrivalType=Location.ENTRANCE;
                arrivalId = -1;
            }
            else{
                arrivalType = null;
                arrivalId = -1;
            }
            return new MoveStudentMessage(nickname, Action.MOVESTUDENT, studentId, departureType, departureId, arrivalType, arrivalId);
        }

        else if(action.equals("MOVEMOTHERNATURE")){
            System.out.println("Insert Mother Nature's movement:");
            MNmovement = stdin.nextInt();
            stdin.nextLine();
            return new MoveMotherNatureMessage(nickname, Action.MOVEMOTHERNATURE, MNmovement);
        }

        else if(action.equals("SELECTCLOUD")){
            System.out.println("Select which cloud you have chosen: (position)");
            position = stdin.nextInt();
            stdin.nextLine();
            return new SelectCloudMessage(nickname, Action.SELECTCLOUD, position);
        }

        return new Message(nickname, null);
    }



    public void showBoard(Game game){
        System.out.println("\n\nCURRENT BOARD:");
        LinkedList<Island> islands = game.getAllIslands();
        for(Island i : islands){
            String owner = "nobody";
            if(i.getOwner() != null){
                owner = String.valueOf(i.getOwner());
            }

            System.out.print("\tIsland "+i+", Owner "+owner+", Towers x"+i.getReport().getTowerNumbers()+", Students: ");
            ArrayList<Student> students = i.getAllStudents();
            for(Student s : students) System.out.print(s+" ");
            if(game.getMotherNaturePosition().equals(i)) System.out.print("MN");
            System.out.print("\n");
        }
        Map<Color, Player> professors = game.getProfessors();
        System.out.print("Professors: ");
        for(Color co : Color.values()) System.out.print(co+": "+professors.get(co)+", ");
        ArrayList<Player> players = game.getAllPlayers();
        System.out.print("\nClouds:\n");
        for(int i=0; i<game.getNumberOfClouds(); i++){
            System.out.print("\tCloud "+i+": ");
            for(Color co : Color.values()) System.out.print(co+"="+game.getNumberOfStudentPerColorOnCloud(i,co)+", ");
            System.out.print("\n");
        }
        for(Player p : players){
            System.out.println("DASHBOARD of "+p+" (color "+p.getDashboard().getColor()+"):");
            if(p.getNickname().equals(nickname)){
                System.out.print("\tAvailable cards: ");
                ArrayList<Card> cards = p.getHand().getAllCards();
                for(Card ca : cards) System.out.print(ca+" ");
                System.out.println("");
            }
            System.out.println("\tLast card played: "+p.getDashboard().getGraveyard());
            System.out.print("\tEntrance: ");
            ArrayList<Student> students = p.getDashboard().getEntrance().getAllStudents();
            for(Student s : students) System.out.print(s+" ");
            System.out.print("\n\tCanteen: ");
            for(Color col : Color.values()){
                System.out.print(col+": "+p.getDashboard().getCanteen().getNumberStudentColor(col)+" ");
            }
            System.out.println("");
        }
        if(game.getCurrentPlayer()==null){
            System.out.println("Waiting for other players...");
        }
        else{
            System.out.println("Turn of: "+game.getCurrentPlayer());
            System.out.println("Phase: "+game.getCurrentPhase());
        }
    }
}