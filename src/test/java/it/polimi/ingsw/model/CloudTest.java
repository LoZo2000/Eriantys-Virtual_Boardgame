package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import it.polimi.ingsw.model.exceptions.StillStudentException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud cloud;
    ArrayList<Student> newStudents;

    @BeforeEach
    void init(){
        this.cloud = new Cloud(3);

        newStudents = new ArrayList<>();
        newStudents.add(new Student(1, Color.RED));
        newStudents.add(new Student(2, Color.BLUE));
        //newStudents.add(new Student(3, Color.GREEN));
        //newStudents.add(new Student(4, Color.YELLOW));
        newStudents.add(new Student(5, Color.PINK));
    }

    @Test
    void refillCloud() {
        try {
            this.cloud.refillCloud(newStudents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(this.newStudents, this.cloud.getStudents());

        assertThrows(StillStudentException.class, () -> {this.cloud.refillCloud(newStudents);});
    }

    @Test
    void getNumberOfStudentPerColor() {
        try {
            this.cloud.refillCloud(newStudents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(1, this.cloud.getNumberOfStudentPerColor(Color.RED));
        assertEquals(1, this.cloud.getNumberOfStudentPerColor(Color.BLUE));
        assertEquals(1, this.cloud.getNumberOfStudentPerColor(Color.PINK));
        assertEquals(0, this.cloud.getNumberOfStudentPerColor(Color.GREEN));
        assertEquals(0, this.cloud.getNumberOfStudentPerColor(Color.YELLOW));

    }

    @Test
    void addStudent() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, Color.GREEN));
        students.add(new Student(2, Color.YELLOW));
        students.add(new Student(3, Color.RED));

        try {
            for(Student s : students){
                this.cloud.addStudent(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(3, this.cloud.getStudents().size());
        assertEquals(students, this.cloud.getStudents());

        assertThrows(TooManyStudentsException.class, () -> {this.cloud.addStudent(new Student(1, Color.BLUE));});
    }

    @Test
    void chooseCloud() {
        try {
            this.cloud.refillCloud(newStudents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        ArrayList<Student> students = this.cloud.chooseCloud();

        assertEquals(this.newStudents, students);
        assertEquals(0, this.cloud.getStudents().size());
    }
}