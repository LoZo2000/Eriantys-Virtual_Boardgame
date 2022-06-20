package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Cloud
 */
class CloudTest {
    Cloud cloud;
    ArrayList<Student> newStudents;

    /**
     * This method is called before each test, it creates a new cloud and adds some students to it
     */
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

    /**
     * This method tests the refill of a Cloud
     */
    @Test
    void refillCloud() {
        try {
            this.cloud.refillCloud(newStudents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(this.newStudents, this.cloud.getStudents());
    }

    /**
     * This method tests the method getNumberOfStudentsPerColor, refilling the Cloud and then checking if the students
     * returned are the same
     */
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

    /**
     * This method tests the method to add a student in a cloud
     */
    @Test
    void addStudent() {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student(1, Color.GREEN));
        students.add(new Student(2, Color.YELLOW));
        students.add(new Student(3, Color.RED));

        try {
            for(Student s : students){
                ArrayList<Student> AL = new ArrayList<>();
                AL.add(s);
                this.cloud.refillCloud(AL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        assertEquals(3, this.cloud.getStudents().size());
        assertEquals(students, this.cloud.getStudents());
    }

    /**
     * This method tests the method chooseCloud, after choosing a cloud check if the number of students returned is zero
     */
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

    /**
     * This method tests other cases of the method refill cloud
     */
    @Test
    public void fillTest(){
        try{
            ArrayList<Student> AL = new ArrayList<>();
            AL.add(new Student(0, Color.BLUE));
            AL.add(new Student(1, Color.BLUE));
            AL.add(new Student(2, Color.BLUE));
            cloud.refillCloud(AL);
        }
        catch(Exception e){fail();}

        ArrayList<Student> s1 = cloud.getStudents();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s1.get(i).getColor());

        ArrayList<Student> s2 = cloud.chooseCloud();
        for(int i=0; i<3; i++)
            assertEquals(Color.BLUE, s2.get(i).getColor());

        ArrayList<Student> s3 = cloud.getStudents();
        assertEquals(0, s3.size());

        try{
            ArrayList<Student> AL = new ArrayList<>();
            AL.add(new Student(4, Color.BLUE));
            cloud.refillCloud(AL);
        }
        catch(Exception e){fail();}
    }
}