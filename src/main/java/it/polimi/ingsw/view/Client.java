package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {

    private String ip;
    private int port;
    private Scanner stdin;

    private Message message;
    private String nickname;
    private int numPlayers, priority, studentId, arrivalId, departureId, MNmovement, position, action, lo, rule;
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
        GameStatus GS;

        stdin = new Scanner(System.in);
        try{

            message = getInputStart();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
            do {
                objectInputStream = new ObjectInputStream(inputStream);
                GS = (GameStatus) objectInputStream.readObject();
                showBoard(GS);
            }while(GS.getEnemiesNames()==null || GS.getEnemiesNames().size()+1<numPlayers || !GS.getTurnOf().equals(nickname));

            while (true){
                message = getInput();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(message);
                do {
                    objectInputStream = new ObjectInputStream(inputStream);
                    GS = (GameStatus) objectInputStream.readObject();
                    showBoard(GS);
                }while(!GS.getTurnOf().equals(nickname));
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            objectOutputStream.close();
            socket.close();
            System.out.println("Thanks for playing!");
        }
    }

    private Message getInputStart(){
        boolean completeRules;
        System.out.println("Welcome in the magic world of Eriantys!\nPlease, insert your nickname:");
        nickname = stdin.nextLine();
        do {
            System.out.println("How many players do you want to play with? (between 2 and 4)");
            numPlayers = stdin.nextInt();
        }while(numPlayers<2 || numPlayers>4);
        System.out.println("Do you want to play by simple (type 0) or complete rules (type 1)?");
        rule = stdin.nextInt();
        if(rule==1) completeRules=true;
        else{
            completeRules=false;
        }
        return new AddMeMessage(nickname, Action.ADDME, completeRules, numPlayers);

    }



    private Message getInput(){
        System.out.println("Select one of the following options (type an integer):");
        System.out.println("\t-'1) PLAYCARD' to play an assistant-card");
        System.out.println("\t-'2) MOVESTUDENT' to move a student from your ENTRANCE to your CANTEEN or an ISLAND");
        System.out.println("\t-'3) MOVEMOTHERNATURE' to move Mother Nature");
        System.out.println("\t-'4) SELECTCLOUD' to refill your ENTRANCE with students from a cloud");
        action = stdin.nextInt();

        switch (action){
            case 1:
                System.out.println("Choose which card do you want to play (priority):");
                priority = stdin.nextInt();
                return new PlayCardMessage(nickname, Action.PLAYCARD, priority);
            case 2:
                System.out.println("Which student do you want to move? (studentId)");
                studentId = stdin.nextInt();
                System.out.println("Where the student is? (type 1 for ENTRANCE, 2 for CANTEEN or 3 for ISLAND)");
                lo = stdin.nextInt();
                switch (lo){
                    case 1:
                        departureType=Location.ENTRANCE;
                        departureId = -1;
                        break;
                    case 2:
                        departureType=Location.CANTEEN;
                        departureId = -1;
                        break;
                    case 3:
                        departureType=Location.ISLAND;
                        System.out.println("Choose island: (islandId)");
                        departureId = stdin.nextInt();
                        break;
                    default:
                        departureType = null;
                        departureId = -1;
                        break;
                }
                System.out.println("Where do you want to move the student? (type 1 for ENTRANCE, 2 for CANTEEN or 3 for ISLAND)");
                lo = stdin.nextInt();
                switch (lo){
                    case 1:
                        arrivalType=Location.ENTRANCE;
                        arrivalId = -1;
                        break;
                    case 2:
                        arrivalType=Location.CANTEEN;
                        arrivalId = -1;
                        break;
                    case 3:
                        arrivalType=Location.ISLAND;
                        System.out.println("Choose island: (islandId)");
                        arrivalId = stdin.nextInt();
                        break;
                    default:
                        arrivalType = null;
                        arrivalId = -1;
                }
                return new MoveStudentMessage(nickname, Action.MOVESTUDENT, studentId, departureType, departureId, arrivalType, arrivalId);
            case 3:
                System.out.println("Insert Mother Nature's movement:");
                MNmovement = stdin.nextInt();
                return new MoveMotherNatureMessage(nickname, Action.MOVEMOTHERNATURE, MNmovement);
            case 4:
                System.out.println("Select which cloud you have chosen: (position)");
                position = stdin.nextInt();
                return new SelectCloudMessage(nickname, Action.SELECTCLOUD, position);
            default:
                return new Message(nickname, null);
        }
    }



    public void showBoard(GameStatus gameStatus){
        if(!gameStatus.getError().equals("")) System.out.println("\n\nFORBIDDEN MOVE: "+gameStatus.getError()+"\nInsert a new legal move...");
        else{
            int towerToWin = 8;
            if(numPlayers==3) towerToWin = 6;
            System.out.println("\n\nISLANDS:");
            for(int i=0; i<gameStatus.getIslandsId().size(); i++){
                System.out.print("Id: "+gameStatus.getIslandsId().get(i)+"\t");
                if(gameStatus.getOwners().get(i)==null) System.out.print("Owner: nobody\t");
                else System.out.print("Owner: "+gameStatus.getOwners().get(i)+"\t");
                System.out.print("Towers: x"+gameStatus.getNumTowers().get(i)+"\t");
                System.out.print("\u001B[34mBLUE: "+gameStatus.getStudentsOnIsland().get(i).get(0)+"\u001B[0m\t");
                System.out.print("\u001B[33mYELLOW: "+gameStatus.getStudentsOnIsland().get(i).get(1)+"\u001B[0m\t");
                System.out.print("\u001B[31mRED: "+gameStatus.getStudentsOnIsland().get(i).get(2)+"\u001B[0m\t");
                System.out.print("\u001B[32mGREEN: "+gameStatus.getStudentsOnIsland().get(i).get(3)+"\u001B[0m\t");
                System.out.print("\u001B[35mPINK: "+gameStatus.getStudentsOnIsland().get(i).get(4)+"\u001B[0m\t");
                if(gameStatus.getMN()==i) System.out.print("MN");
                System.out.print("\n");
            }
            System.out.println("CLOUDS:");
            for(int i=0; i<gameStatus.getStudentsOnCloud().size(); i++){
                System.out.print("Cloud "+i+":\t");
                System.out.print("\u001B[34mBLUE: "+gameStatus.getStudentsOnCloud().get(i).get(0)+"\u001B[0m\t");
                System.out.print("\u001B[33mYELLOW: "+gameStatus.getStudentsOnCloud().get(i).get(1)+"\u001B[0m\t");
                System.out.print("\u001B[31mRED: "+gameStatus.getStudentsOnCloud().get(i).get(2)+"\u001B[0m\t");
                System.out.print("\u001B[32mGREEN: "+gameStatus.getStudentsOnCloud().get(i).get(3)+"\u001B[0m\t");
                System.out.print("\u001B[35mPINK: "+gameStatus.getStudentsOnCloud().get(i).get(4)+"\u001B[0m\n");

            }
            System.out.print("PROFESSORS:\t");
            System.out.print("\u001B[34mBLUE: "+gameStatus.getProfessorsOwners().get(0)+"\u001B[0m\t");
            System.out.print("\u001B[33mYELLOW: "+gameStatus.getProfessorsOwners().get(1)+"\u001B[0m\t");
            System.out.print("\u001B[31mRED: "+gameStatus.getProfessorsOwners().get(2)+"\u001B[0m\t");
            System.out.print("\u001B[32mGREEN: "+gameStatus.getProfessorsOwners().get(3)+"\u001B[0m\t");
            System.out.print("\u001B[35mPINK: "+gameStatus.getProfessorsOwners().get(4)+"\u001B[0m\n");
            if(gameStatus.getEnemiesNames()!=null){
                for(int i=0; i<gameStatus.getEnemiesNames().size(); i++){
                    System.out.println("DASHBOARD of "+gameStatus.getEnemiesNames().get(i)+" (color: "+gameStatus.getEnemiesColors().get(i)+")\tLast card: "+gameStatus.getEnemiesLastCards().get(i)+"\tTowers: "+(towerToWin-gameStatus.getEnemiesTowers().get(i)));
                    System.out.print("Entrance:\t");
                    System.out.print("\u001B[34mBLUE: "+gameStatus.getEnemiesEntrances().get(i).get(0)+"\u001B[0m\t");
                    System.out.print("\u001B[33mYELLOW: "+gameStatus.getEnemiesEntrances().get(i).get(1)+"\u001B[0m\t");
                    System.out.print("\u001B[31mRED: "+gameStatus.getEnemiesEntrances().get(i).get(2)+"\u001B[0m\t");
                    System.out.print("\u001B[32mGREEN: "+gameStatus.getEnemiesEntrances().get(i).get(3)+"\u001B[0m\t");
                    System.out.print("\u001B[35mPINK: "+gameStatus.getEnemiesEntrances().get(i).get(4)+"\u001B[0m\n");
                    System.out.print("Canteen:\t");
                    System.out.print("\u001B[34mBLUE: "+gameStatus.getEnemiesCanteen().get(i).get(0)+"/10\u001B[0m\t");
                    System.out.print("\u001B[33mYELLOW: "+gameStatus.getEnemiesCanteen().get(i).get(1)+"/10\u001B[0m\t");
                    System.out.print("\u001B[31mRED: "+gameStatus.getEnemiesCanteen().get(i).get(2)+"/10\u001B[0m\t");
                    System.out.print("\u001B[32mGREEN: "+gameStatus.getEnemiesCanteen().get(i).get(3)+"/10\u001B[0m\t");
                    System.out.print("\u001B[35mPINK: "+gameStatus.getEnemiesCanteen().get(i).get(4)+"/10\u001B[0m\n");
                }
            }
            System.out.println("YOUR DASHBOARD (color: "+gameStatus.getMyColor()+")\tLast card: "+gameStatus.getMyLastCard()+"\tTowers: "+(towerToWin-gameStatus.getMyTowers()));
            System.out.println("Your cards: "+gameStatus.getMyCards());
            System.out.println("Entrance:\t"+gameStatus.getStudentsInEntrance()+"\u001B[0m");
            System.out.print("Canteen:\t");
            System.out.print("\u001B[34mBLUE: "+gameStatus.getStudentsInCanteen().get(0)+"\u001B[0m\n");
            System.out.print("\t\t\t\u001B[33mYELLOW: "+gameStatus.getStudentsInCanteen().get(1)+"\u001B[0m\n");
            System.out.print("\t\t\t\u001B[31mRED: "+gameStatus.getStudentsInCanteen().get(2)+"\u001B[0m\n");
            System.out.print("\t\t\t\u001B[32mGREEN: "+gameStatus.getStudentsInCanteen().get(3)+"\u001B[0m\n");
            System.out.print("\t\t\t\u001B[35mPINK: "+gameStatus.getStudentsInCanteen().get(4)+"\u001B[0m\n");
            System.out.println("Current phase: "+gameStatus.getCurrentPhase());
            System.out.println("Current player: "+gameStatus.getTurnOf());
        }
    }
}