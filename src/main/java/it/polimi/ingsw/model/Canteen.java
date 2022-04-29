package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;
import java.util.ArrayList;

public class Canteen implements Movable{
    private ArrayList<Student> blue;
    private ArrayList<Student> yellow;
    private ArrayList<Student> red;
    private ArrayList<Student> green;
    private ArrayList<Student> pink;

    private boolean giveCoin;
    private Color removedColor;

    public Canteen(){
        blue = new ArrayList<>();
        yellow = new ArrayList<>();
        red = new ArrayList<>();
        green = new ArrayList<>();
        pink = new ArrayList<>();

        this.giveCoin = false;
        this.removedColor = null;
    }

    public void addStudent(Student s){
        switch (s.getColor()){
            case BLUE -> blue.add(s);
            case YELLOW -> yellow.add(s);
            case RED -> red.add(s);
            case GREEN -> green.add(s);
            case PINK -> pink.add(s);
        }

        this.giveCoin = true;
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

    public Student removeStudent(int id) throws NoSuchStudentException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent;
        int positionInList;

        tempStudent = new Student(id, Color.RED);
        positionInList = this.red.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.RED;

            Student removedStudent = this.red.get(positionInList);
            this.red.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.BLUE);
        positionInList = this.blue.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.BLUE;

            Student removedStudent = this.blue.get(positionInList);
            this.blue.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.GREEN);
        positionInList = this.green.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.GREEN;

            Student removedStudent = this.green.get(positionInList);
            this.green.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.PINK);
        positionInList = this.pink.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.PINK;

            Student removedStudent = this.pink.get(positionInList);
            this.pink.remove(positionInList);
            return removedStudent;
        }

        tempStudent = new Student(id, Color.YELLOW);
        positionInList = this.yellow.indexOf(tempStudent);
        if(positionInList != -1) {
            this.giveCoin = true;
            this.removedColor = Color.YELLOW;

            Student removedStudent = this.yellow.get(positionInList);
            this.yellow.remove(positionInList);
            return removedStudent;
        }

        throw new NoSuchStudentException("There isn't any Student with that id in the lists");
    }

    //Reset is a flag that set to false the giveCoin boolean. When we exchange 2 students this flag can't be reset
    //for the first color because we don't know if the first Movable in the moveStudents method was the Canteen or
    //the Entrance.
    public boolean canGetCoin(Color c, boolean reset){
        boolean result = false;
        if(this.giveCoin && this.getNumberStudentColor(c) % 3 == 0 && this.getNumberStudentColor(c) != 0 && this.removedColor != c){
            result = true;
        }
        if(reset) {
            this.giveCoin = false;
            this.removedColor = null;
        }

        return result;
    }
}