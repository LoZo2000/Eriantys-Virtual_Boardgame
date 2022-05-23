package it.polimi.ingsw.model;

import java.io.Serializable;
import static org.fusesource.jansi.Ansi.*;

public class Student implements Serializable {
    private final int id;
    private final Color color;
    private String sprite;

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

    public int getId(){
        return id;
    }
    public Color getColor(){
        return this.color;
    }
    public String getSprite(){
        return sprite;
    }

    public boolean equals(Object o){
        if(o instanceof Student){
            if(((Student) o).getId() == id) return true;
        }
        return false;
    }

    public String toString(){
        /*String c = "";
        switch (color){
            case BLUE -> c = "\u001B[34m";
            case YELLOW -> c = "\u001B[33m";
            case RED -> c = "\u001B[31m";
            case GREEN -> c = "\u001B[32m";
            case PINK -> c = "\u001B[95m";
        }
        return c+String.valueOf(id)+"\u001B[0m";*/
        return ansi().fgBright(color.ansiColor).a(id).reset().toString();
    }
}