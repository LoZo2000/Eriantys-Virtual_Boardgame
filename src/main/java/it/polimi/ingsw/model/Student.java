package it.polimi.ingsw.model;

public class Student {
    private final int id;
    private Color color;

    public Student(int id, Color color){
        this.id = id;
        this.color = color;
    }

    public int getId(){
        return id;
    }
    public Color getColor(){
        return this.color;
    }
}
