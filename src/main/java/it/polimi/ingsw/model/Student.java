package it.polimi.ingsw.model;

import java.io.Serializable;

public class Student implements Serializable {
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

    public boolean equals(Object o){
        if(o instanceof Student){
            if(((Student) o).getId() == id) return true;
        }
        return false;
    }

    public String toString(){
        String c = "";
        switch (color){
            case BLUE -> c = "\u001B[34m";
            case YELLOW -> c = "\u001B[33m";
            case RED -> c = "\u001B[31m";
            case GREEN -> c = "\u001B[32m";
            case PINK -> c = "\u001B[35m";
        }
        return c+String.valueOf(id)+"\u001B[0m";
    }
}