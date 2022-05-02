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
    private String action;
    private String rule, lo;
    private int numPlayers, priority, studentId, studentId2, arrivalId, departureId, MNmovement, position, numCard;
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
        Game game;

        //AnsiConsole.systemInstall();

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

            while (true) {
                message = getInput();
                //System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));
                //clearConsole();
                if (message != null) {
                    objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);
                    do {
                        objectInputStream = new ObjectInputStream(inputStream);
                        game = (Game) objectInputStream.readObject();
                        showBoard(game);
                        activeCard = game.getActiveCard();
                        if(activeCard != -1){
                            requestedAction = game.getRequestedAction();
                        } else{
                            requestedAction = null;
                        }
                    } while (game.getCurrentPlayer() == null || !game.getCurrentPlayer().equals(nickname));
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
            AnsiConsole.systemUninstall();
        }
    }

    private Message getInputStart(){
        //boolean completeRules;
        /*System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));*/

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
        return new AddMeMessage(nickname, completeRules, numPlayers);

    }



    private Message getInput(){
        System.out.println("Select one of the following options:");
        if(activeCard == -1) {
            System.out.println("    -'PLAYCARD' to play an assistant-card");
            System.out.println("    -'MOVESTUDENT' to move a student from your ENTRANCE to your CANTEEN or an ISLAND");
            System.out.println("    -'MOVEMOTHERNATURE' to move Mother Nature");
            System.out.println("    -'SELECTCLOUD' to refill your ENTRANCE with students from a cloud");
            if(completeRules) {
                System.out.println("    -'USEPOWER' to select a character card and activate its power.");
            }
        } else{
            System.out.println("    -'"+requestedAction+"' "+movesDescription.get(requestedAction));
        }
        action = stdin.nextLine();

        if (action.equals("PLAYCARD") ) {
            System.out.println("Choose which card do you want to play (priority):");
            try {
                priority = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return null;
            }
            return new PlayCardMessage(nickname, priority);
        } else if (action.equals("MOVESTUDENT")) {
            System.out.println("Which student do you want to move? (studentId)");
            try {
                studentId = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return null;
            }
            System.out.println("Where the student is? (Location)");
            lo = stdin.nextLine().toUpperCase();
            try {
                departureType = Location.valueOf(lo);
            } catch (IllegalArgumentException e) {
                System.out.println("The inserted value isn't a valid location");
                return null;
            }
            departureId = -1;
            if (lo.equals("ISLAND")) {
                System.out.println("Choose island: (islandId)");
                try {
                    departureId = Integer.parseInt(stdin.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("The inserted value isn't a valid number");
                    return null;
                }
            }
            //sc.nextLine();
            System.out.println("Where do you want to move the student? (Location)");
            lo = stdin.nextLine().toUpperCase();
            try {
                arrivalType = Location.valueOf(lo);
            } catch (IllegalArgumentException e) {
                System.out.println("The inserted value isn't a valid location");
            }
            arrivalId = -1;
            if (lo.equals("ISLAND")) {
                System.out.println("Choose island: (islandId)");
                try {
                    arrivalId = Integer.parseInt(stdin.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("The inserted value isn't a valid number");
                    return null;
                }
            }
            return new MoveStudentMessage(nickname, studentId, departureType, departureId, arrivalType, arrivalId);

        } else if(action.equals("USEPOWER")){
            System.out.print("Choose which card you want to activate (0,1,2): ");
            numCard = -1;
            try {
                numCard = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return null;
            }
            return new UsePowerMessage(nickname, numCard);
        } else if (action.equals("MOVEMOTHERNATURE")) {
            System.out.println("Insert Mother Nature's movement:");
            try {
                MNmovement = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return null;
            }
            return new MoveMotherNatureMessage(nickname, MNmovement);
        } else if (action.equals("SELECTCLOUD")) {
            System.out.println("Select which cloud you have chosen: (position)");
            try {
                position = Integer.parseInt(stdin.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("The inserted value isn't a valid number");
                return null;
            }
            return new SelectCloudMessage(nickname, position);
        } else if(activeCard != -1){
            switch(action){
                case "EXCHANGESTUDENT" -> {
                    System.out.print("Which student do you want to move? (studentId) ");
                    try {
                        studentId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }

                    System.out.println("Where is the student? (Location) ");
                    lo = stdin.nextLine().toUpperCase();
                    try {
                        departureType = Location.valueOf(lo);
                    } catch (IllegalArgumentException e) {
                        System.out.println("The inserted value isn't a valid location");
                        return null;
                    }
                    departureId = -1;

                    System.out.println("Which is the other student do you want to move? (studentId) ");
                    try {
                        studentId2 = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }

                    System.out.println("Where is this other student? (Location) ");
                    lo = stdin.nextLine().toUpperCase();
                    try {
                        arrivalType = Location.valueOf(lo);
                    } catch (IllegalArgumentException e) {
                        System.out.println("The inserted value isn't a valid location");
                    }
                    arrivalId = -1;

                    return new ExchangeStudentMessage(nickname, studentId, studentId2, departureType, departureId, arrivalType, arrivalId);
                }
                case "ISLAND_INFLUENCE" -> {
                    System.out.println("Choose island: (islandId)");
                    try {
                        arrivalId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new IslandInfluenceMessage(nickname, arrivalId);
                }
                case "BLOCK_ISLAND" -> {
                    System.out.println("Choose island: (islandId)");
                    try {
                        arrivalId = Integer.parseInt(stdin.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("The inserted value isn't a valid number");
                        return null;
                    }
                    return new BlockIslandMessage(nickname, arrivalId);
                }
                case "BLOCK_COLOR" -> {
                    System.out.println("Choose color: (Color)");
                    lo = stdin.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid color");
                        return null;
                    }

                    return new BlockColorMessage(nickname, chosenColor);
                }
                case "PUT_BACK" -> {
                    System.out.println("Choose color: (Color)");
                    lo = stdin.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(lo);
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