package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;
import it.polimi.ingsw.model.exceptions.StillStudentException;

import java.util.ArrayList;

public class Cloud{
    private final int maxStudents;
    ArrayList<Student> students;

    public Cloud(int maxStudents){
        this.maxStudents = maxStudents;
        students = new ArrayList<>();
    }

    public void refillCloud(ArrayList<Student> newStudents) throws TooManyStudentsException, StillStudentException {
        if(students.size()!=0) throw new StillStudentException("Clouds are not empty");
        for(Student s : newStudents){
            addStudent(s);
        }
    }

    public int getNumberOfStudentPerColor(Color color){
        int[] colors = {0,0,0,0,0};
        for(Student s : students){
            switch (s.getColor()){
                case BLUE -> colors[0]++;
                case YELLOW -> colors[1]++;
                case RED -> colors[2]++;
                case GREEN -> colors[3]++;
                case PINK -> colors[4]++;
            }
        }
        switch(color){
            case BLUE: return colors[0];
            case YELLOW: return colors[1];
            case RED: return colors[2];
            case GREEN: return colors[3];
            case PINK: return colors[4];
            default: return 1;
        }
    }

    //To pass students one at a time
    public void addStudent(Student student) throws TooManyStudentsException {
        if(students.size()<maxStudents) students.add(student);
        else throw new TooManyStudentsException();
    }

    //Returns a copy of students (to let the users examine the cloud)
    public ArrayList<Student> getStudents(){
        return (ArrayList<Student>)students.clone();
    }

    //Returns the students and empties the cloud
    public ArrayList<Student> chooseCloud(){
        ArrayList<Student> s = (ArrayList<Student>)students.clone();
        students.clear();
        return s;
    }
}
