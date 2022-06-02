package it.polimi.ingsw.model;

import java.io.Serializable;
import static org.fusesource.jansi.Ansi.*;

/**
 * The class Student represent the student pawn in the game, the attribute of the class are id to identify the student,
 * the color, and the sprite, the methods of the class are principally getter and an override of the method equals and toString
 */
public class Student implements Serializable {
    private final int id;
    private final Color color;
    private String sprite;

    /**
     * Constructor of the entity Student
     * @param id is the identification number of the student
     * @param color is the color of the student
     */
    public Student(int id, Color color){
        this.id = id;
        this.color = color;
        switch (color){
            case BLUE -> sprite = "/Stud_blue.png";
            case YELLOW -> sprite = "/Stud_yellow.png";
            case RED -> sprite = "/Stud_red.png";
            case GREEN -> sprite = "/Stud_green.png";
            case PINK -> sprite = "/Stud_pink.png";
        }
    }

    /**
     * method that return the id of the student
     * @return an int that represent the id of the student
     */
    public int getId(){
        return id;
    }

    /**
     * method that return the id of the student
     * @return an enum Color that represent the color of the student
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * method that return the address of the sprite of the students
     * @return a string represnting the address of the location of the sprite
     */
    public String getSprite(){
        return sprite;
    }

    /**
     * Override of the equal method of student, two student are equals if have the same id
     * @param o is the object to compare with the student
     * @return true if the objects are equal
     */
    public boolean equals(Object o){
        if(o instanceof Student){
            if(((Student) o).getId() == id) return true;
        }
        return false;
    }

    /**
     * Override of the toString method of Student
     * @return the color of the id of the student colored with is color in ansi standard
     */
    public String toString(){
        return ansi().fgBright(color.ansiColor).a(id).reset().toString();
    }
}