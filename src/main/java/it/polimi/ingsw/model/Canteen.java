package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;

import java.util.ArrayList;
import java.util.List;

public class Canteen {
    List<Student> blue;
    List<Student> yellow;
    List<Student> red;
    List<Student> green;
    List<Student> pink;

    public Canteen(){
        blue = new ArrayList<Student>();
        yellow = new ArrayList<Student>();
        red = new ArrayList<Student>();
        green = new ArrayList<Student>();
        pink = new ArrayList<Student>();
    }

    public void addStudent(Student s){
        switch (s.getColor()){
            case BLUE -> blue.add(s);
            case YELLOW -> yellow.add(s);
            case RED -> red.add(s);
            case GREEN -> green.add(s);
            case PINK -> pink.add(s);
        }
    }

    public int getNumberStudentColor(Color c){
        switch (c){
            case BLUE:
                return blue.size();
            case YELLOW:
                return yellow.size();
            case RED:
                return red.size();
            case GREEN:
                return green.size();
            default: //case PINK
                return pink.size();
        }
    }

    public Student removeStudent(Color c) throws NoMoreStudentsException {
        switch(c){
            case BLUE:
                if(blue.size()>0){
                    Student s = blue.get(0);
                    blue.remove(0);
                    return s;
                }
                else throw new NoMoreStudentsException();
            case YELLOW:
                if(yellow.size()>0){
                    Student s = yellow.get(0);
                    yellow.remove(0);
                    return s;
                }
                else throw new NoMoreStudentsException();
            case RED:
                if(red.size()>0){
                    Student s = red.get(0);
                    red.remove(0);
                    return s;
                }
                else throw new NoMoreStudentsException();
            case GREEN:
                if(green.size()>0){
                    Student s = green.get(0);
                    green.remove(0);
                    return s;
                }
                else throw new NoMoreStudentsException();
            default: //case PINK
                if(pink.size()>0){
                    Student s = pink.get(0);
                    pink.remove(0);
                    return s;
                }
                else throw new NoMoreStudentsException();
        }
    }
}