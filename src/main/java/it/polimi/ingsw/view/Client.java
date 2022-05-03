package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {

    private String ip;
    private int port;
    private Scanner stdin;

    private Message message;
    private String nickname;
    private int numPlayers, priority, studentId, studentId2, arrivalId, departureId, MNmovement, position, numCard, action, lo, rule;
    private Location arrivalType, departureType;
    private Color chosenColor;
    private boolean completeRules;
    private final Map<Action, String> movesDescription;
    private int activeCard = -1;
    private Action requestedAction = null;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;

        movesDescription = new HashMap<>();
        movesDescription.put(Action.MOVESTUDENT, "to move a students from the CARD to the CANTEEN of the player or an ISLAND, depending on the active card.");
        movesDescription.put(Action.EXCHANGESTUDENT, "to exchange two students between the CARD and the ENTRANCE or between the ENTRANCE and the CANTEEN.");
        movesDescription.put(Action.BLOCK_ISLAND, "to put a token on an Island that block the influence calculation when Mother Nature arrives on that island.");
        movesDescription.put(Action.BLOCK_COLOR, "to choose a color that won't be considered during the influence calculation in this turn.");
        movesDescription.put(Action.PUT_BACK, "to choose a color of students: up to 3 students of that color for each player will be put back in the bag.");
        movesDescription.put(Action.ISLAND_INFLUENCE, "to choose an Island of which will be calculated the influence without moving Mother Nature.");
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        Scanner socketIn = new Scanner(socket.getInputStream());
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream;
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = null;
        GameStatus gameStatus;

        //AnsiConsole.systemInstall();

        activeCard = -1;

        stdin = new Scanner(System.in);
        try{
            message = getInputStart();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
            do {
                objectInputStream = new ObjectInputStream(inputStream);
                gameStatus = (GameStatus) objectInputStream.readObject();
                showBoard(gameStatus);
            }while(gameStatus.getEnemiesNames()==null || gameStatus.getEnemiesNames().size()+1<numPlayers || gameStatus.getTurnOf() == null ||!gameStatus.getTurnOf().equals(nickname));

            while (true) {
                message = getInput();
                if (message != null) {
                    objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                    do {
                        objectInputStream = new ObjectInputStream(inputStream);
                        gameStatus = (GameStatus) objectInputStream.readObject();
                        showBoard(gameStatus);
                        //activeCard = gameStatus.getActiveCard();
                        if(activeCard != -1){
                            //requestedAction = gameStatus.getRequestedAction();
                        } else{
                            requestedAction = null;
                        }
                    }while(!gameStatus.getTurnOf().equals(nickname));
                }
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
            AnsiConsole.systemUninstall();
        }
    }

    private Message getInputStart(){
        //boolean completeRules;
        /*System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));*/
        boolean completeRules;
        System.out.println("Welcome in the magic world of Eriantys!\nPlease, insert your nickname:");
        nickname = stdin.nextLine();
        do {
            System.out.println("How many players do you want to play with? (a number between 2 and 4)");
            try{
                numPlayers = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e){
                numPlayers = -1;
            }
        }while(numPlayers<2 || numPlayers>4);

        System.out.println("Do you want to play by simple (type 0) or complete rules (type 1)?");
        rule = stdin.nextInt();
        stdin.nextLine();

        completeRules = rule == 1;

        return new AddMeMessage(nickname, completeRules, numPlayers);
    }



    private Message getInput(){
        System.out.println("Select one of the following options (type an integer):");
        if(activeCard == -1) {
            System.out.println("\t-'1) PLAYCARD' to play an assistant-card");
            System.out.println("\t-'2) MOVESTUDENT' to move a student from your ENTRANCE to your CANTEEN or an ISLAND");
            System.out.println("\t-'3) MOVEMOTHERNATURE' to move Mother Nature");
            System.out.println("\t-'4) SELECTCLOUD' to refill your ENTRANCE with students from a cloud");

            if(completeRules) {
                System.out.println("\t-'5) USEPOWER' to select a character card and activate its power.");
            }
        } else{
            System.out.println("\t-'1) "+requestedAction+"' "+movesDescription.get(requestedAction));
        }
        try {
            action = Integer.parseInt(stdin.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("The inserted value isn't a valid number");
            return null;
        }

        if(activeCard == -1) {
            switch (action) {
                case 1:
                    System.out.println("Choose which card do you want to play (priority):");
                    try {
                        priority = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new PlayCardMessage(nickname, priority);
                case 2:
                    System.out.println("Which student do you want to move? (studentId)");
                    try {
                        studentId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    System.out.println("Where is the student? (type 1 for ENTRANCE, 2 for CANTEEN, 3 for ISLAND, 4 for CARD_ISLAND, 5 for CARD_CANTEEN, 6 for CARD_EXCHANGE)");
                    try {
                        lo = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    departureType = getLocation(lo);
                    departureId = getLocationId(departureType);
                    //sc.nextLine();
                    System.out.println("Where do you want to move the student? (type 1 for ENTRANCE, 2 for CANTEEN, 3 for ISLAND, 4 for CARD_ISLAND, 5 for CARD_CANTEEN, 6 for CARD_EXCHANGE)");
                    try {
                        lo = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    arrivalType = getLocation(lo);
                    arrivalId = getLocationId(arrivalType);

                    return new MoveStudentMessage(nickname, studentId, departureType, departureId, arrivalType, arrivalId);
                case 3:
                    System.out.println("Insert Mother Nature's movement:");
                    try {
                        MNmovement = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new MoveMotherNatureMessage(nickname, MNmovement);
                case 4:
                    System.out.println("Select which cloud you have chosen: (position)");
                    try {
                        position = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new SelectCloudMessage(nickname, position);
                case 5:
                    if (completeRules) {
                        System.out.print("Choose which card you want to activate (0,1,2): ");
                        numCard = -1;
                        try {
                            numCard = Integer.parseInt(stdin.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("The inserted value isn't a valid number");
                            return null;
                        }
                        return new UsePowerMessage(nickname, numCard);
                    } else {
                        System.out.println("You can't use characters card with simple rules");
                        return null;
                    }
                default:
                    System.out.println("Invalid action!");
                    return null;
            }
        } else{
            switch(requestedAction){
                case EXCHANGESTUDENT -> {
                    System.out.print("Which student do you want to move? (studentId) ");
                    try {
                        studentId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }

                    System.out.println("Where is the student? (type 1 for ENTRANCE, 2 for CANTEEN, 3 for ISLAND, 4 for CARD_ISLAND, 5 for CARD_CANTEEN, 6 for CARD_EXCHANGE)");
                    try {
                        lo = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    departureType = getLocation(lo);
                    departureId = getLocationId(departureType);

                    System.out.println("Which is the other student do you want to move? (studentId) ");
                    try {
                        studentId2 = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }

                    System.out.println("Where is the this second student? (type 1 for ENTRANCE, 2 for CANTEEN, 3 for ISLAND, 4 for CARD_ISLAND, 5 for CARD_CANTEEN, 6 for CARD_EXCHANGE)");
                    try {
                        lo = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    arrivalType = getLocation(lo);
                    arrivalId = getLocationId(arrivalType);

                    return new ExchangeStudentMessage(nickname, studentId, studentId2, departureType, departureId, arrivalType, arrivalId);
                }
                case ISLAND_INFLUENCE -> {
                    System.out.println("Choose island: (islandId)");
                    try {
                        arrivalId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new IslandInfluenceMessage(nickname, arrivalId);
                }
                case BLOCK_ISLAND -> {
                    System.out.println("Choose island: (islandId)");
                    try {
                        arrivalId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new BlockIslandMessage(nickname, arrivalId);
                }
                case BLOCK_COLOR -> {
                    System.out.println("Choose color: (Color)");
                    String color = stdin.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(color);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid color");
                        return null;
                    }

                    return new BlockColorMessage(nickname, chosenColor);
                }
                case PUT_BACK -> {
                    System.out.println("Choose color: (Color)");
                    String color = stdin.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(color);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid color");
                        return null;
                    }

                    return new PutBackMessage(nickname, chosenColor);
                }
            }
        }

        return null;
    }

    private Location getLocation(int location){
        switch (location) {
            case 1 -> {
                return Location.ENTRANCE;
            }
            case 2 -> {
                return Location.CANTEEN;
            }
            case 3 -> {
                return Location.ISLAND;
            }
            case 4 -> {
                return Location.CARD_ISLAND;
            }
            case 5 -> {
                return Location.CARD_CANTEEN;
            }
            case 6 -> {
                return Location.CARD_EXCHANGE;
            }
            default -> {
                return null;
            }
        }
    }

    private int getLocationId(Location type){
        int id;
        if(type == Location.ISLAND){
            System.out.println("Choose island: (islandId)");
            try {
                id = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return -1;
            }
        } else{
            id = -1;
        }

        return id;
    }

    public void showBoard(Game game){
        //clearConsole();
        /*System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));*/

        Columns col = new Columns();

        col.addColumn(1,"\n\u001B[31;1mCURRENT BOARD:\u001B[0m");
        col.addColumn(2,"");
        col.addColumn(1,"ISLANDS:");
        col.addColumn(2,"");
        LinkedList<Island> islands = game.getAllIslands();
        boolean leftRight = true;
        for(Island i : islands){
            String owner = "nobody";
            if(i.getOwner() != null){
                owner = String.valueOf(i.getOwner());
            }
            String islandString = "";
            islandString += "    Island "+i+", Owner "+owner+", Towers x"+i.getReport().getTowerNumbers()+", Students: ";
            ArrayList<Student> students = i.getAllStudents();
            for(Student s : students) islandString += s+" ";
            if(game.getMotherNaturePosition().equals(i)) islandString += "MN";
            if(i.getProhibition()) islandString += "\u001B[31mBLOCK\u001B[0m";

            if(leftRight)
                col.addColumn(1, islandString);
            else
                col.addColumn(2, islandString);

            leftRight = !leftRight;
        }
        Map<Color, Player> professors = game.getProfessors();
        String profs = "\nProfessors: ";
        for(Color co : Color.values()) profs += co+": "+professors.get(co)+", ";
        col.addColumn(1, profs);
        ArrayList<Player> players = game.getAllPlayers();
        col.addColumn(1, "\nClouds:");
        for(int i=0; i<game.getNumberOfClouds(); i++){
            String cloudString = "";
            cloudString += "    Cloud "+i+": ";
            for(Color co : Color.values()) cloudString += co+"="+game.getNumberOfStudentPerColorOnCloud(i,co)+", ";
            col.addColumn(1, cloudString);
        }
        col.addColumn(1, "\n");
        for(Player p : players){
            String playerString = "";
            playerString += "\u001B[1mDASHBOARD of "+p+"\u001B[0m (color "+p.getDashboard().getColor()+"):\n";
            if(p.getNickname().equals(nickname)){
                playerString += "    Available cards: \n";
                ArrayList<Card> cards = p.getHand().getAllCards();
                for(Card ca : cards) playerString += ca+" ";
                playerString += "\n";
            }
            playerString += "    Last card played: "+p.getDashboard().getGraveyard() + "\n";
            playerString += "    Entrance: ";
            ArrayList<Student> students = p.getDashboard().getEntrance().getAllStudents();
            for(Student s : students) playerString += s+" ";
            playerString += "\n\u001B[1m\u001B[37mCANTEEN: \u001B[0m\n";
            for (Color colo : Color.values()) {
                playerString += "    \u001B[1m"+colo + "\u001B[0m: " + p.getDashboard().getCanteen().getStudents(colo) + " (Num: " + p.getDashboard().getCanteen().getNumberStudentColor(colo) +")\n";
            }
            if(completeRules)
                playerString +="\n    Coins: " + p.getCoins();
            playerString += "\n";
            col.addColumn(1, playerString);
        }
        if(completeRules){
            String cardsString = "";
            Character[] characters = game.getCharactersCards();
            cardsString += "\nCARDS: \n";
            for (Character c : characters) {
                cardsString += c.toString() + "\n";
            }

            if (game.getActiveCard() == -1) {
                cardsString += "\u001B[31mNo active card\u001B[0m\n";
            } else {
                cardsString += "\u001B[34mThe active card is " + game.getActiveCard() + "\u001B[0m\n";
                try {
                    cardsString += "\u001B[34mThe nextAction is " + game.getRequestedAction() + "\u001B[0m\n";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            col.addColumn(2, cardsString);
        }

        col.printAll();

        if(game.getCurrentPlayer()==null){
            System.out.println("Waiting for other players...");
        }
        else{
            System.out.println("Turn of: "+game.getCurrentPlayer());
            System.out.println("Phase: "+game.getCurrentPhase());
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

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {}
    }
}