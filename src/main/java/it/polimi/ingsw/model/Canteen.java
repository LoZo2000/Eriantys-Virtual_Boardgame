package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.util.ArrayList;

public class Canteen implements Movable, Cloneable{
    ArrayList<Student> blue;
    ArrayList<Student> yellow;
    ArrayList<Student> red;
    ArrayList<Student> green;
    ArrayList<Student> pink;

    public Canteen(){
        blue = new ArrayList<>();
        yellow = new ArrayList<>();
        red = new ArrayList<>();
        green = new ArrayList<>();
        pink = new ArrayList<>();
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

    public ArrayList<Student> getStudents(Color c){
        switch (c){
            case BLUE:
                return (ArrayList<Student>)blue.clone();
            case YELLOW:
                return (ArrayList<Student>)yellow.clone();
            case RED:
                return (ArrayList<Student>)red.clone();
            case GREEN:
                return (ArrayList<Student>)green.clone();
            default: //case PINK
                return (ArrayList<Student>)pink.clone();
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

    //IndexOf!!!!!!!!!!!!!!!!!!!!!!!!!!
    /*public Student removeStudent(int id) throws NoSuchStudentException {
        Student s = null;
        for(int i=0; i < blue.size(); i++){
            if (blue.get(i).getId() == id){
                s = blue.get(i);
                blue.remove(i);
            }
        }
        for(int i=0; i < yellow.size(); i++){
            if (yellow.get(i).getId() == id){
                s = yellow.get(i);
                yellow.remove(i);
            }
        }
        for(int i=0; i < red.size(); i++){
            if (red.get(i).getId() == id){
                s = red.get(i);
                red.remove(i);
            }
        }
        for(int i=0; i < green.size(); i++){
            if (green.get(i).getId() == id){
                s = green.get(i);
                green.remove(i);
            }
        }
        for(int i=0; i< pink.size(); i++){
            if (pink.get(i).getId() == id){
                s = pink.get(i);
                pink.remove(i);
            }
        }
        if(s == null) throw new NoSuchStudentException();
        return s;
    }*/
    public Student removeStudent(int id) throws NoSuchStudentException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent;
        int positionInList;

        tempStudent = new Student(id, Color.RED);
        positionInList = this.red.indexOf(tempStudent);
        if(positionInList != -1) {
            Student removedStudent = this.red.get(positionInList);
            this.red.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.BLUE);
        positionInList = this.blue.indexOf(tempStudent);
        if(positionInList != -1) {
            Student removedStudent = this.blue.get(positionInList);
            this.blue.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.GREEN);
        positionInList = this.green.indexOf(tempStudent);
        if(positionInList != -1) {
            Student removedStudent = this.green.get(positionInList);
            this.green.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.PINK);
        positionInList = this.pink.indexOf(tempStudent);
        if(positionInList != -1) {
            Student removedStudent = this.pink.get(positionInList);
            this.pink.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.YELLOW);
        positionInList = this.yellow.indexOf(tempStudent);
        if(positionInList != -1) {
            Student removedStudent = this.yellow.get(positionInList);
            this.yellow.remove(positionInList);
            return removedStudent;
        }

        throw new NoSuchStudentException("There isn't any Student with that id in the lists");
    }

    public Canteen getCanteen() {
        try {
            return (Canteen)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}