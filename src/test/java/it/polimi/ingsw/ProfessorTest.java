package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Professor;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class ProfessorTest extends TestCase {

    @Test
    public void getColorTest(){
        for(Color c : Color.values()){
            Professor p = new Professor(c);
            assertEquals(c, p.getColor());
        }
    }
}
