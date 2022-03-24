package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;

import java.util.ArrayList;

public class Cloud{
    private final int maxStudents;
    ArrayList<Student> students;

    public Cloud(int maxStudents){
        this.maxStudents = maxStudents;
        students = new ArrayList<>();
    }

    public void fillCloud(ArrayList<Student> newStudents) throws TooManyStudentsException {
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
