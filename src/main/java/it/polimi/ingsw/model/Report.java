package it.polimi.ingsw.model;

import java.util.Map;

public class Report {
    //private final String id;
    private final ColorTower owner;
    private final int towerNumbers;
    private final Map<Color, Integer> students;

    private final ColorTower extraPointReceiver;
    private final int extraPoint;

    public Report(ColorTower owner, int towerNumbers, Map<Color, Integer> students, ColorTower extraPointReceiver, int extrapoints){
        //this.id = id;
        this.owner= owner;
        this.towerNumbers=towerNumbers;
        this.students= students;
        this.extraPointReceiver = extraPointReceiver;
        this.extraPoint = extrapoints;
    }

    //public String getId(){ return id; }

    public ColorTower getOwner(){ return owner; }

    public int getTowerNumbers(){ return towerNumbers; }

    public int getColorStudents(Color c){
        return students.get(c);
    }

    public ColorTower getExtraPointReceiver(){
        return extraPointReceiver;
    }

    public int getExtraPoint(){
        return extraPoint;
    }
}
