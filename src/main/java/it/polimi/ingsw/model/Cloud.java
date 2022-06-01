package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * The class cloud represent the cloud in the game, the cloud store the student until they are choosen at the end
 * of the turn, a cloud can be full or not, depending if it was already choose in the turn, the most important methods in the
 * class cloud are refill cloud to refill the students in the cloud and choosecloud to permit the player to choose the cloud
 */
public class Cloud {
    private final int maxStudents;

    private boolean isFull;
    private ArrayList<Student> students;

    /**
     * Creator of the entity Cloud
     * @param maxStudents is the maximum number of students the cloud can contain. It depends from
     *                    the number of players in the match
     */
    public Cloud(int maxStudents){
        this.maxStudents = maxStudents;
        this.isFull = false;
        students = new ArrayList<>();
    }

    /**
     * Method to refill cloud with 3 or 4 Students, according to the number of players
     * @param newStudents ArrayList containing the new students to be added
     */
    public void refillCloud(ArrayList<Student> newStudents) {
        for(Student s : newStudents){
            addStudent(s);
        }
        this.isFull = true;
    }

    /**
     * Method to return how many students of a Color are available on this Cloud
     * @param color is the color of the students you are interested in
     * @return an integer representing the number of students of that color on this Cloud
     */
    public int getNumberOfStudentPerColor(Color color){
        long contStudents = this.students.stream()
                .filter((s) -> s.getColor() == color)
                .count();
        return (int) contStudents;
    }

    private void addStudent(Student student) {
        if(students.size()<maxStudents) students.add(student);
    }

    /**
     * Returns a copy of the students on the Cloud (to let the users to examine the cloud)
     * @return an ArrayList containing the copy of all these students
     */
    public ArrayList<Student> getStudents(){
        return (ArrayList<Student>)students.clone();
    }

    /**
     * Returns the students on the cloud and empties the cloud
     * @return these students
     */
    public ArrayList<Student> chooseCloud(){
        ArrayList<Student> s = (ArrayList<Student>)students.clone();
        students.clear();
        this.isFull = false;
        return s;
    }

    /**
     * To check if a cloud is full (and therefore to check if a cloud has been already selected
     * from another player)
     * @return true if the cloud has still students on it, false elsewhere
     */
    public boolean isFull(){
        return isFull;
    }

    /**
     * To set the value of the attribute isFull
     * @param isFull is a boolean value to be assigned to the private attribute isFull
     */
    public void setIsFull(Boolean isFull){
        this.isFull=isFull;
    }
}