package it.polimi.ingsw;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.GameHandler2;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.exceptions.EndGameException;
import it.polimi.ingsw.controller.exceptions.IllegalMessageException;
import it.polimi.ingsw.messages.*;

import java.util.Scanner;

public class CLITest2 {

    /*public static void main(String[] args) throws IllegalMessageException {
        String playerId, ac, lo, ans;
        Location arrivalType, departureType;
        boolean completeRules;
        int numPlayers, priority, studentId, arrivalId, departureId, MNmovement, position;

        int numCard, studentId2;
        Color chosenColor;

        Scanner sc=new Scanner(System.in);

        Message message = new CREATEMATCHmessage("abc", Action.CREATEMATCH, true, 2 );
        GameHandler2 gameHandler = new GameHandler2(message);


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
                    message = new AddMeMessage(playerId, Action.ADDME, true, 2);
                case "PLAYCARD" -> {
                    System.out.println("Chose which card you want to play (priority):");
                    priority = sc.nextInt();
                    sc.nextLine();
                    message = new PLAYCARDmessage(playerId, Action.PLAYCARD, priority);
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
                    message = new MOVESTUDENTmessage(playerId, Action.MOVESTUDENT, studentId, departureType, departureId, arrivalType, arrivalId);
                }
                case "USEPOWER" -> {
                    //sc.nextLine();
                    System.out.print("Choose which card you want to activate (0,1,2): ");
                    numCard = sc.nextInt();
                    sc.nextLine();
                    message = new USEPOWERmessage(playerId, Action.USEPOWER, numCard);
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

                    message = new EXCHANGESTUDENTmessage(playerId, Action.EXCHANGESTUDENT, studentId, studentId2, departureType, departureId, arrivalType, arrivalId);

                }
                case "ISLAND_INFLUENCE" -> {
                    System.out.println("Choose island: (islandId)");
                    arrivalId = sc.nextInt();
                    sc.nextLine();

                    message = new ChooseIslandMessage(playerId, Action.ISLAND_INFLUENCE, arrivalId);
                }
                case "BLOCK_ISLAND" -> {
                    System.out.println("Choose island: (islandId)");
                    arrivalId = sc.nextInt();
                    sc.nextLine();

                    message = new ChooseIslandMessage(playerId, Action.BLOCK_ISLAND, arrivalId);
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

                    message = new ChooseColorMessage(playerId, Action.BLOCK_COLOR, chosenColor);
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

                    message = new ChooseColorMessage(playerId, Action.PUT_BACK, chosenColor);
                }
                case "MOVEMOTHERNATURE" -> {
                    System.out.println("Insert Mother Nature's movement:");
                    MNmovement = sc.nextInt();
                    sc.nextLine();
                    message = new MOVEMOTHERNATUREmessage(playerId, Action.MOVEMOTHERNATURE, MNmovement);
                }
                case "SELECTCLOUD" -> {
                    System.out.println("Select which cloud you have chosen: (position)");
                    position = sc.nextInt();
                    sc.nextLine();
                    message = new SELECTCLOUDmessage(playerId, Action.SELECTCLOUD, position);
                }
                case "SHOWME" -> message = new SHOWMEmessage(playerId, Action.SHOWME);
                default -> message = new Message(playerId, null);
            }

            try{
                gameHandler.execute(message);
            }catch(Exception e){
                e.printStackTrace();
                if(e instanceof EndGameException) System.exit(0);
            }
        }
    }*/
}