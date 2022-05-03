package it.polimi.ingsw;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameHandler;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.EndGameException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.view.Columns;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class CLITest2 {

    /*@Test
    public void badClient() throws Exception {
        Socket socket = new Socket("127.0.0.1", 12346);
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = null;
        Message message = new PlayCardMessage("aaa", 1);
        objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(message);

    }*/

    public static void main(String[] args) throws IllegalMessageException {
        Message message;
        if(AnsiConsole.getTerminalWidth() != 0)
            AnsiConsole.systemInstall();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));

        String playerId, ac, lo, ans;
        Location arrivalType, departureType;
        boolean completeRules;
        int numPlayers, priority, studentId, arrivalId, departureId, MNmovement, position;

        int numCard, studentId2;
        Color chosenColor;

        Scanner sc=new Scanner(System.in);

        Game game = new Game(true, 2);
        GameHandler gameHandler = new GameHandler(game);


        while(true){
            //sc.nextLine();
            System.out.println("La fase attuale è "+gameHandler.getPhase());
            System.out.println("Current player (player's nickname):");
            playerId = sc.nextLine();
            //sc.nextLine();
            System.out.println("Insert action: (Action)");
            ac = sc.nextLine().toUpperCase();

            switch (ac) {
                case "ADDME" ->
                    //sc.nextLine();
                    //System.out.println("Do you want to play with complete rules? (y/n)");
                    //ans = sc.nextLine();
                    //completeRules = false;
                    //if(ans.equals("y")) completeRules = true;
                    //System.out.println("How many players?");
                    //numPlayers = sc.nextInt();
                    //sc.nextLine();
                        message = new AddMeMessage(playerId, true, 2);
                case "PLAYCARD" -> {
                    System.out.println("Chose which card you want to play (priority):");
                    priority = sc.nextInt();
                    sc.nextLine();
                    message = new PlayCardMessage(playerId, priority);
                }
                case "MOVESTUDENT" -> {
                    System.out.println("Which student do you want to move? (studentId)");
                    studentId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Where the student is? (Location)");
                    lo = sc.nextLine().toUpperCase();
                    try {
                        departureType = Location.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid location");
                        continue;
                    }
                    departureId = -1;
                    if (lo.equals("ISLAND")) {
                        System.out.println("Choose island: (islandId)");
                        departureId = sc.nextInt();
                        sc.nextLine();
                    }
                    //sc.nextLine();
                    System.out.println("Where do you want to move the student? (Location)");
                    lo = sc.nextLine().toUpperCase();
                    try {
                        arrivalType = Location.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid location");
                        continue;
                    }
                    arrivalId = -1;
                    if (lo.equals("ISLAND")) {
                        System.out.println("Choose island: (islandId)");
                        arrivalId = sc.nextInt();
                        sc.nextLine();
                    }
                    message = new MoveStudentMessage(playerId, studentId, departureType, departureId, arrivalType, arrivalId);
                }
                case "USEPOWER" -> {
                    //sc.nextLine();
                    System.out.print("Choose which card you want to activate (0,1,2): ");
                    numCard = sc.nextInt();
                    sc.nextLine();
                    message = new UsePowerMessage(playerId, numCard);
                }
                case "EXCHANGESTUDENT" -> {
                    System.out.print("Which student do you want to move? (studentId) ");
                    studentId = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Where is the student? (Location) ");
                    lo = sc.nextLine().toUpperCase();
                    try {
                        departureType = Location.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid location");
                        continue;
                    }
                    departureId = -1;

                    System.out.print("Which is the other student do you want to move? (studentId) ");
                    studentId2 = sc.nextInt();
                    sc.nextLine();
                    //sc.nextLine();
                    System.out.println("Where is this other student? (Location) ");
                    lo = sc.nextLine().toUpperCase();

                    try {
                        arrivalType = Location.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid location");
                        continue;
                    }
                    arrivalId = -1;

                    message = new ExchangeStudentMessage(playerId, studentId, studentId2, departureType, departureId, arrivalType, arrivalId);

                }
                case "ISLAND_INFLUENCE" -> {
                    System.out.println("Choose island: (islandId)");
                    arrivalId = sc.nextInt();
                    sc.nextLine();

                    message = new IslandInfluenceMessage(playerId, arrivalId);
                }
                case "BLOCK_ISLAND" -> {
                    System.out.println("Choose island: (islandId)");
                    arrivalId = sc.nextInt();
                    sc.nextLine();

                    message = new BlockIslandMessage(playerId, arrivalId);
                }
                case "BLOCK_COLOR" -> {
                    System.out.println("Choose color: (Color)");
                    lo = sc.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid color");
                        continue;
                    }

                    message = new BlockColorMessage(playerId, chosenColor);
                }
                case "PUT_BACK" -> {
                    System.out.println("Choose color: (Color)");
                    lo = sc.nextLine().toUpperCase();

                    try {
                        chosenColor = Color.valueOf(lo);
                    }catch (IllegalArgumentException e){
                        System.out.println("The inserted value isn't a valid color");
                        continue;
                    }

                    message = new PutBackMessage(playerId, chosenColor);
                }
                case "MOVEMOTHERNATURE" -> {
                    System.out.println("Insert Mother Nature's movement:");
                    MNmovement = sc.nextInt();
                    sc.nextLine();
                    message = new MoveMotherNatureMessage(playerId, MNmovement);
                }
                case "SELECTCLOUD" -> {
                    System.out.println("Select which cloud you have chosen: (position)");
                    position = sc.nextInt();
                    sc.nextLine();
                    message = new SelectCloudMessage(playerId, position);
                }
                case "SHOWME" -> message = new ShowMeMessage(playerId, Action.SHOWME);
                default -> message = null;
            }

            try{
                System.out.print(ansi().eraseScreen(Ansi.Erase.ALL).cursor(1,1));
                gameHandler.execute(message);
            }catch(Exception e){
                e.printStackTrace();
                if(e instanceof EndGameException) {
                    System.exit(0);
                    AnsiConsole.systemUninstall();
                }
            }
        }
    }
}