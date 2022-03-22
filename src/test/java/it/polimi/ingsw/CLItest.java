package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Scanner;

public class CLItest {

    @Test
    public void play(){
        Scanner sc=new Scanner(System.in);
        int opz;
        Game game;

        while(true) {
            System.out.println("\n\nSELECT ONE OF THE FOLLOWING OPTIONS:");
            System.out.println("0)Initialize game");
            System.out.println("1)Get all islands");
            System.out.println("2)Where is MotherNature");
            System.out.println("3)Get students from an island");
            System.out.println("4)Get students in entrance");
            System.out.println("5)Get students in canteen");
            System.out.println("6)Get a player's hand");
            System.out.println("7)Move student");
            System.out.println("8)Move MotherNature");
            System.out.println("9)Merge islands");
            System.out.println("10)Get all clouds");
            System.out.println("11)Choose cloud");
            System.out.println("12)Refill clouds");
            System.out.println("\n\n");
            opz = sc.nextInt();

            switch(opz){
                case 0:
                    game = new Game(false, 3);
                    //game.addPlayer("player1");
                    //game.addPlayer("player2");
                    //game.addPlayer("player3");
                    //game.addPlayer("");
                    break;
                case 1:
                    //ArrayList<Island> islands = game.getIslands();
                    //for(Island i : islands) System.out.println(i.getId());
                    break;
                case 2:
                    //System.out.println(game.getMotherNaturePosition().getId());
                    break;
                case 3:
                    System.out.println("Insert id island:");
                    int islandId;
                    islandId = sc.nextInt();
                    //ArrayList<Student> students = game.getIsland(islandId).getAllStudents();
                    //for(Student s : students) System.out.println(s.getId());
                    break;
                case 4:
                    System.out.println("Insert id player:");
                    int playerId;
                    playerId = sc.nextInt();
                    //ArrayList<Student> students = game.getPlayer(playerId).getDashboard().getEntrance().getAllStudents();
                    //for(Student s : students) System.out.println(s.getId());
                    break;
                case 5:
                    System.out.println("Insert id player & color:");
                    int playerId;
                    playerId = sc.nextInt();
                    String color;
                    Color c;
                    color = sc.nextLine();
                    switch(color){
                        case "blue" -> c=Color.BLUE;
                        case "yellow" -> c=Color.YELLOW;
                        case "red" -> c=Color.RED;
                        case "green" -> c=Color.GREEN;
                        case "pink" -> c=Color.PINK;
                    }
                    //ArrayList<Student> students = game.getPlayer(playerId).getDashboard().getCanteen().getStudents(c);
                    //for(Student s : students) System.out.println(s.getId());
                    break;
                case 6:
                    System.out.println("Insert id player:");
                    int playerId;
                    playerId = sc.nextInt();
                    //ArrayList<Card> cards = game.getPlayer(playerId).getHand().getAllCards;
                    //for(Card c : cards){
                        // System.out.println(c.getPriority());
                        // System.out.println(c.getMovement());
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    System.out.println("Insert id first island:");
                    int i1Id;
                    i1Id = sc.nextInt();
                    System.out.println("Insert id second island:");
                    int i2Id;
                    i2Id = sc.nextInt();
                    Island i1;
                    Island i2;
                    //ArrayList<Island> islands = game.getAllIslands();
                    //for(Island i : islands){
                    //    if(i.getId() == i1Id) i1 = i;
                    //    if(i.getId() == i2Id) i2 = i;
                    //}
                    //game.mergeIsland(i1, i2);
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                default:
                    System.out.println("This option doesn't exist...");
            }
        }
    }
}