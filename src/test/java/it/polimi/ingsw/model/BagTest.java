package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the test for the class Bag
 */
public class BagTest{
    private Bag bag;

    /**
     * This method is called before each test, creates a bag containing 120 students
     */
    @BeforeEach
    public void init(){
        Stack<Student> students = new Stack<>();
        students.push(new Student(0, Color.BLUE));
        students.push(new Student(1, Color.YELLOW));
        students.push(new Student(2, Color.RED));
        students.push(new Student(3, Color.GREEN));
        students.push(new Student(4, Color.PINK));
        this.bag = new Bag(students);
    }


    /**
     * This method check if the method getStudentsNum works correctly
     */
    @Test
    public void checkStudentsNum(){
        for(int i=0; i<5; i++){
            try {
                Student s = bag.getRandomStudent();
            }
            catch(Exception e){
                fail();
            }
        }

        assertThrows(NoMoreStudentsException.class, () -> bag.getRandomStudent());
        assertThrows(NoMoreStudentsException.class, () -> bag.getRandomStudent(2));
    }
}