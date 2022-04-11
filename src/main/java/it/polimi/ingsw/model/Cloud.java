package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.TooManyStudentsException;
import it.polimi.ingsw.model.exceptions.StillStudentException;

import java.io.Serializable;
import java.util.ArrayList;

public class Cloud implements Serializable {
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
        long contStudents = this.students.stream()
                .filter((s) -> s.getColor() == color)
                .count();
        return (int) contStudents;
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
