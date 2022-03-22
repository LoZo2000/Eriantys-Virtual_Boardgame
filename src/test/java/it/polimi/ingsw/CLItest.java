package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;
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
            System.out.println("SELECT ONE OF THE FOLLOWING OPTIONS:");
            System.out.println("0)Initialize game");
            System.out.println("1)Get islands");
            System.out.println("2)Where is MotherNature");
            System.out.println("3 + islandId)Get students from an island");
            System.out.println("4 + playerNick)Get entrance");
            System.out.println("5 + playerNick)Get canteen");
            System.out.println("6 +playerId)Get your hand");
            System.out.println("7 + from + to)Move student");
            System.out.println("8 + from + to)Move MotherNature");
            System.out.println("9 + island1Id + island2Id)Merge islands");
            System.out.println("10 + cloudId)Choose cloud");
            opz = sc.nextInt();

            switch(opz){
                case 0:
                    game = new Game("myGame", 3);
                    game.addPlayer("player2");
                    game.addPlayer("");
                    //game.init(3);
                    break;
                case 1:
                    //ArrayList<Island> islands = game.getIslands();
                    //for(int i=0; i<islands.size(); i++) System.out.println(islands.get(i).getId());
                    break;
                case 2:
                    //System.out.println(game.getMotherNaturePosition().getId());
                    break;
                case 3:
                    int islandId;
                    islandId = sc.nextInt();
                    //ArrayList<Student> students = game.getIsland(islandId).getAllStudents();
                    //for(int i=0; i<students.size(); i++) System.out.println(students.get(i).getId());
                    break;
            }
        }
    }
}