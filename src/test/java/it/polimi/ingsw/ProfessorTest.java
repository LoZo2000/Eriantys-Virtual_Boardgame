package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Professor;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProfessorTest {

    @Test
    public void getColorTest(){
        for(Color c : Color.values()){
            Professor p = new Professor(c);
            assertEquals(c, p.getColor());
        }
    }
}
