package it.polimi.ingsw.model;

import java.util.Map;

public class Report {
    private final ColorTower owner;
    private final int towerNumbers;
    private final Map<Color, Integer> students;

    public Report(ColorTower owner, int towerNumbers, Map<Color, Integer> students){
        this.owner= owner;
        this.towerNumbers=towerNumbers;
        this.students= students;
    }

    public ColorTower getOwner(){ return owner; }

    public int getTowerNumbers(){ return towerNumbers; }

    public int getColorStudents(Color c){
        return students.get(c);
    }
}
