package it.polimi.ingsw.model;

public class Report {
    private final ColorTower owner;
    private final int towerNumbers;
    private final int blueStudents;
    private final int yellowStudents;
    private final int redStudents;
    private final int greenStudents;
    private final int pinkStudents;

    public Report(ColorTower owner, int towerNumbers, int blueStudents, int yellowStudents, int redStudents, int greenStudents, int pinkStudents){
        this.owner= owner;
        this.towerNumbers=towerNumbers;
        this.blueStudents= blueStudents;
        this.yellowStudents= yellowStudents;
        this.redStudents=redStudents;
        this.greenStudents=greenStudents;
        this.pinkStudents=pinkStudents;
    }

    public ColorTower getOwner(){ return owner; }

    public int getTowerNumbers(){ return towerNumbers; }

    public int getColorStudents(Color c){
        switch(c){
            case BLUE:
                return blueStudents;
            case YELLOW:
                return yellowStudents;
            case RED:
                return redStudents;
            case GREEN:
                return greenStudents;
            default: //case PINK
                return pinkStudents;
        }
    }
}
