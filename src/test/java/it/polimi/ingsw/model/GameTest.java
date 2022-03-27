package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;

    @BeforeEach
    void init(){
        ArrayList<Student> students1 = new ArrayList<>();
        students1.add(new Student(1, Color.RED));
        students1.add(new Student(2, Color.BLUE));
        students1.add(new Student(3, Color.GREEN));
        students1.add(new Student(4, Color.PINK));
        students1.add(new Student(5, Color.BLUE));
        students1.add(new Student(6, Color.BLUE));
        students1.add(new Student(7, Color.BLUE));

        Player player1 = new Player("player1", ColorTower.BLACK, students1);

        ArrayList<Student> students2 = new ArrayList<>();
        students2.add(new Student(8, Color.RED));
        students2.add(new Student(9, Color.BLUE));
        students2.add(new Student(10, Color.GREEN));
        students2.add(new Student(11, Color.PINK));
        students2.add(new Student(12, Color.YELLOW));
        students2.add(new Student(13, Color.RED));
        students2.add(new Student(14, Color.BLUE));

        Player player2 = new Player("player2", ColorTower.WHITE, students2);

        this.game = new Game(false, 2);

        this.game.addPlayer(player1);
        this.game.addPlayer(player2);
    }

    @Test
    void moveMotherNature() {
        this.game.moveStudent(0, 1, "player1", 6, 0);
        this.game.moveStudent(0, 2, "player1", 2, 3);
        this.game.moveStudent(0, 2, "player1", 7, 3);

        Island i = this.game.getIsland(3);
        this.game.moveMotherNature(i);

        assertEquals(ColorTower.BLACK, i.getReport().getOwner());
        assertEquals(1, i.getReport().getTowerNumbers());

        this.game.moveStudent(0, 2, "player1", 5, 4);
        i = this.game.getIsland(4);
        this.game.moveMotherNature(i);

        assertEquals(11, this.game.getAllIslands().size());
    }

    @Test
    void updateProfessorsMoveToCanteen(){
        this.game.moveStudent(0, 1, "player1", 2, 0);
        Map<Color, Player> professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));
    }

    @Test
    void updateProfessorsNotMoveToCanteen(){
        this.game.moveStudent(0, 2, "player1", 2, 0);
        Map<Color, Player> professors = this.game.getProfessors();

        assertNull(professors.get(Color.BLUE));
    }

    @Test
    void updateProfessorsChangeOwnership(){
        this.game.moveStudent(0, 1, "player1", 2, 0);
        Map<Color, Player> professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player1"), professors.get(Color.BLUE));

        this.game.moveStudent(0, 1, "player2", 9, 0);
        this.game.moveStudent(0, 1, "player2", 14, 0);
        professors = this.game.getProfessors();

        assertEquals(this.game.getPlayer("player2"), professors.get(Color.BLUE));
    }
}