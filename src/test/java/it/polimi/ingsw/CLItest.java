package it.polimi.ingsw;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class CLItest {

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int opz;
        Game game=null;
        LinkedList<Island> islands;
        ArrayList<Student> students;
        ArrayList<Card> cards;
        int islandId;
        int studentId;
        String playerNick;

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
                    game.addPlayer("player1");
                    game.addPlayer("player2");
                    game.addPlayer("player3");
                    break;
                case 1:
                    islands = game.getAllIslands();
                    for(Island i : islands) System.out.println(i.getId());
                    break;
                case 2:
                    System.out.println(game.getMotherNaturePosition().getId());
                    break;
                case 3:
                    System.out.println("Insert id island:");
                    islandId = sc.nextInt();
                    students = game.getIsland(islandId).getAllStudents();
                    for(Student s : students) System.out.println(s.getId());
                    break;
                case 4:
                    sc.nextLine();
                    System.out.println("Insert id player:");
                    playerNick = sc.nextLine();
                    students = game.getPlayer(playerNick).getDashboard().getEntrance().getAllStudents();
                    for(Student s : students) System.out.println(s.getId());
                    break;
                case 5:
                    sc.nextLine();
                    System.out.println("Insert id player & color:");
                    playerNick = sc.nextLine();
                    String color;
                    Color c;
                    color = sc.nextLine();
                    switch(color){
                        case "blue" -> c=Color.BLUE;
                        case "yellow" -> c=Color.YELLOW;
                        case "red" -> c=Color.RED;
                        case "green" -> c=Color.GREEN;
                        case "pink" -> c=Color.PINK;
                        default -> c=null;
                    }
                    students = game.getPlayer(playerNick).getDashboard().getCanteen().getStudents(c);
                    for(Student s : students) System.out.println(s.getId());
                    break;
                case 6:
                    sc.nextLine();
                    System.out.println("Insert id player:");
                    playerNick = sc.nextLine();
                    cards = game.getPlayer(playerNick).getHand().getAllCards();
                    for(Card car : cards) {
                        System.out.println(car.getPriority());
                        System.out.println(car.getMovement());
                    }
                    break;
                case 7:
                    int from, to, id;
                    Movable departure;
                    Movable arrival;

                    System.out.println("What is student's id?");
                    id = sc.nextInt();

                    System.out.println("Locations: 0=entrance, 1=canteen, 2=island");
                    System.out.println("Where is the student (select location)?");
                    from = sc.nextInt();
                    switch(from){
                        case 0:
                            sc.nextLine();
                            System.out.println("Select player:");
                            playerNick = sc.nextLine();
                            departure = game.getPlayer(playerNick).getDashboard().getEntrance();
                            break;
                        case 1:
                            sc.nextLine();
                            System.out.println("Select player:");
                            playerNick = sc.nextLine();
                            departure = game.getPlayer(playerNick).getDashboard().getCanteen();
                            break;
                        case 2:
                            System.out.println("Select island:");
                            islandId = sc.nextInt();
                            departure = game.getIsland(islandId);
                            break;
                        default:
                            departure = null;
                            System.out.println("Something has gone wrong...");
                    }

                    System.out.println("Where do you want to move it (select location)?");
                    to = sc.nextInt();
                    switch(to){
                        case 0:
                            sc.nextLine();
                            System.out.println("Select player:");
                            playerNick = sc.nextLine();
                            arrival = game.getPlayer(playerNick).getDashboard().getEntrance();
                            break;
                        case 1:
                            sc.nextLine();
                            System.out.println("Select player:");
                            playerNick = sc.nextLine();
                            arrival = game.getPlayer(playerNick).getDashboard().getCanteen();
                            break;
                        case 2:
                            System.out.println("Select island:");
                            islandId = sc.nextInt();
                            arrival = game.getIsland(islandId);
                            break;
                        default:
                            arrival = null;
                            System.out.println("Something has gone wrong...");
                    }

                    Student s;
                    try{
                        s = departure.removeStudent(id);
                        arrival.addStudent(s);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    System.out.println("Select new position of MotherNature:");
                    islandId = sc.nextInt();
                    islands = game.getAllIslands();
                    for(Island i : islands){
                        if(i.getId() == islandId){
                            game.moveMotherNature(i);
                        }
                    }
                    break;
                case 9:
                    System.out.println("Insert id first island:");
                    int i1Id;
                    i1Id = sc.nextInt();
                    System.out.println("Insert id second island:");
                    int i2Id;
                    i2Id = sc.nextInt();
                    Island i1=null;
                    Island i2=null;
                    LinkedList<Island> islandstemp = game.getAllIslands();
                    for(Island i : islandstemp){
                        if(i.getId() == i1Id) i1 = i;
                        if(i.getId() == i2Id) i2 = i;
                    }
                    try {
                        game.mergeIsland(i1, i2);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                /*case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;*/
                default:
                    System.out.println("This option doesn't exist...");
            }
        }
    }
}