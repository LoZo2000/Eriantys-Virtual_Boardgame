package it.polimi.ingsw.model;

public class Report {
    private ColorTower owner;
    private int towerNumbers;
    private int blueStudents;
    private int yellowStudents;
    private int redStudents;
    private int greenStudents;
    private int pinkStudents;

    public Report(ColorTower owner, int towerNumbers, int blueStudents, int yellowStudents, int redStudents, int greenStudents, int pinkStudents){
        this.owner= owner;
        this.blueStudents= blueStudents;
        this.yellowStudents= yellowStudents;
        this.redStudents=redStudents;
        this.greenStudents=greenStudents;
        this.pinkStudents=pinkStudents;
        this.towerNumbers=towerNumbers;
    }

    public ColorTower getOwner(){ return owner; }

    public int getTowerNumbers(){ return towerNumbers; }

    public int getBlueStudents(){ return blueStudents; }

    public int getYellowStudents(){ return yellowStudents; }

    public int getRedStudents(){ return redStudents; }

    public int getGreenStudents(){ return greenStudents; }

    public int getPinkStudents(){ return pinkStudents;}

}
