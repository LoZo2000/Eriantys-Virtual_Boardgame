package it.polimi.ingsw;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ColorTower;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Tower;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TowerTest {

    @Test
    public void getterTest(){
        for(int i=0; i<50; i++){
            for(ColorTower c : ColorTower.values()){
                Tower t = new Tower(i, c);
                assertEquals(i, t.getId());
                assertEquals(c, t.getColorTower());
            }
        }
    }
}