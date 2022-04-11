package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EndGameException;
import it.polimi.ingsw.messages.*;

import java.util.Scanner;

public class CLItest {

    /*public static void main(String[] args){
        String playerId, ac, lo, ans;
        Location arrivalType, departureType;
        boolean completeRules;
        int numPlayers, priority, studentId, arrivalId, departureId, MNmovement, position;
        Scanner sc=new Scanner(System.in);

        Message message;
        GameHandler gameHandler = new GameHandler();

        while(true){
            //sc.nextLine();
            System.out.println("Current player (player's nickname):");
            playerId = sc.nextLine();
            //sc.nextLine();
            System.out.println("Insert action: (Action)");
            ac = sc.nextLine();

            if(ac.equals("ADDME")) {
                //sc.nextLine();
                System.out.println("Do you want to play with complete rules? (y/n)");
                ans = sc.nextLine();
                completeRules = false;
                if(ans.equals("y")) completeRules = true;
                System.out.println("How many players?");
                numPlayers = sc.nextInt();
                sc.nextLine();
                message = new ADDMEmessage(playerId, Action.ADDME, completeRules, numPlayers);
            }

            else if(ac.equals("PLAYCARD")){
                System.out.println("Chose which card you want to play (priority):");
                priority = sc.nextInt();
                sc.nextLine();
                message = new PLAYCARDmessage(playerId, Action.PLAYCARD, priority);
            }

            else if(ac.equals("MOVESTUDENT")){
                System.out.println("Which student do you want to move? (studentId)");
                studentId = sc.nextInt();
                sc.nextLine();
                System.out.println("Where the student is? (Location)");
                lo = sc.nextLine();
                if(lo.equals("ISLAND")){
                    departureType=Location.ISLAND;
                    System.out.println("Choose island: (islandId)");
                    departureId = sc.nextInt();
                    sc.nextLine();
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
                //sc.nextLine();
                System.out.println("Where do you want to move the student? (Location)");
                lo = sc.nextLine();
                if(lo.equals("ISLAND")){
                    arrivalType=Location.ISLAND;
                    System.out.println("Choose island: (islandId)");
                    arrivalId = sc.nextInt();
                    sc.nextLine();
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
                message = new MOVESTUDENTmessage(playerId, Action.MOVESTUDENT, studentId, departureType, departureId, arrivalType, arrivalId);
            }

            else if(ac.equals("MOVEMOTHERNATURE")){
                System.out.println("Insert Mother Nature's movement:");
                MNmovement = sc.nextInt();
                sc.nextLine();
                message = new MOVEMOTHERNATUREmessage(playerId, Action.MOVEMOTHERNATURE, MNmovement);
            }

            else if(ac.equals("SELECTCLOUD")){
                System.out.println("Select which cloud you have chosen: (position)");
                position = sc.nextInt();
                sc.nextLine();
                message = new SELECTCLOUDmessage(playerId, Action.SELECTCLOUD, position);
            }

            else if(ac.equals("SHOWME")){
                message = new SHOWMEmessage(playerId, Action.SHOWME);
            }

            else{
                message = new Message(playerId, null);
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