package it.polimi.ingsw.model;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;

    @BeforeEach
    public void init(){
        ArrayList<Student> students = new ArrayList<>();
        for(int i=0; i<7; i++){
            Color c = null;
            int caseColor = i%5;
            switch (caseColor) {
                case 0 -> c = Color.RED;
                case 1 -> c = Color.BLUE;
                case 2 -> c = Color.GREEN;
                case 3 -> c = Color.PINK;
                case 4 -> c = Color.YELLOW;
            }
            Student s = new Student(i, c);
            students.add(s);
        }
        Collections.shuffle(students);
        player = new Player("player1", 2, ColorTower.BLACK, students);
    }

    @Test
    public void getter(){
        assertEquals("player1", player.getNickname());
        assertEquals(8, player.getDashboard().getTowers());
        assertEquals(10, player.getHand().getNumCards());
    }

    @Test
    public void equalsTest(){
        assertTrue(player.equals(player));
        assertFalse(player.equals(new Student(3, Color.BLUE)));

        ArrayList<Student> students = new ArrayList<>();
        for(int i=7; i<14; i++){
            Color c = null;
            int caseColor = i%5;
            switch (caseColor) {
                case 0 -> c = Color.RED;
                case 1 -> c = Color.BLUE;
                case 2 -> c = Color.GREEN;
                case 3 -> c = Color.PINK;
                case 4 -> c = Color.YELLOW;
            }
            Student s = new Student(i, c);
            students.add(s);
        }

        Player temp = new Player("player2", 2, ColorTower.WHITE, students);
        assertNotEquals(player, temp);
    }

    @Test
    void comparatorTest(){
        ArrayList<Player> players = new ArrayList<>();

        Player p1 = newPlayer("p1", 3, ColorTower.BLACK);
        Player p2 = newPlayer("p2", 3, ColorTower.WHITE);
        Player p3 = newPlayer("p3", 3, ColorTower.GREY);

        players.add(p1);
        players.add(p2);
        players.add(p3);

        p1.getDashboard().setGraveyard(new Card(4,2));
        p2.getDashboard().setGraveyard(new Card(10,5));
        p3.getDashboard().setGraveyard(new Card(1, 1));

        Collections.sort(players);

        assertEquals(p3, players.get(0));
        assertEquals(p1, players.get(1));
        assertEquals(p2, players.get(2));
    }

    @Test
    public void toStringTest(){
        System.out.println("The player is "+player);
    }

    private Player newPlayer(String name, int numPlayer, ColorTower color){
        ArrayList<Student> students = new ArrayList<>();
        for(int i=0; i<7; i++){
            Color c = null;
            int caseColor = i%5;
            switch (caseColor) {
                case 0 -> c = Color.RED;
                case 1 -> c = Color.BLUE;
                case 2 -> c = Color.GREEN;
                case 3 -> c = Color.PINK;
                case 4 -> c = Color.YELLOW;
            }
            Student s = new Student(i, c);
            students.add(s);
        }
        Collections.shuffle(students);
        return new Player(name, numPlayer, color, students);
    }
}
