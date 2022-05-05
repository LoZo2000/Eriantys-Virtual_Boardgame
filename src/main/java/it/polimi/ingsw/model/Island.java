package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NoSuchStudentException;

import java.io.Serializable;
import java.util.*;

public class Island implements Movable {
    //private final int id;
    private final String id;
    private ArrayList<Student> students;
    private boolean prohibitionToken;
    private int maxTowers;
    private ColorTower owner;

    public Island(int id){
        //this.id = id;
        this.id = String.format("%02d", id);
        students = new ArrayList<>();
        maxTowers = 1;
        prohibitionToken = false;
        this.owner = null;
    }

    public Island(Island i1, Island i2){
        //this(i1.id);
        students = new ArrayList<>();
        String s = i1.id + ", " + i2.id;

        String[] arr = s.split(", ");

        this.id = Arrays.stream(arr)
                .map(Integer::parseInt)
                .sorted()
                .map(x -> String.format("%02d", x))
                .reduce((temp1, temp2) -> temp1 + ", " + temp2)
                .orElse(null);

        students.addAll(i1.students);
        students.addAll(i2.students);
        maxTowers = i1.maxTowers + i2.maxTowers;
        owner = i1.owner;
        prohibitionToken = i1.prohibitionToken || i2.prohibitionToken;
    }

    public String getId(){
        return id;
    }

    public ArrayList<Integer> getStudentsId(Color color, int numberOfStudents){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        int cont=0;
        for(Student s : students){
            if(cont>=numberOfStudents) break;
            if(s.getColor() == color){
                ids.add(s.getId());
                cont++;
            }
        }
        return ids;
    }

    public ColorTower getOwner(){
        return this.owner;
    }

    public Report getReport(){
        int numTowers;
        if(owner==null){
            numTowers=0;
        }
        else { numTowers=maxTowers;}

        Map<Color, Integer> colors = new HashMap<>();
        int[] colorsCont = {0, 0, 0, 0, 0};
        for(Student s: students){
            switch (s.getColor()){
                case BLUE -> colorsCont[0]++;
                case YELLOW -> colorsCont[1]++;
                case RED -> colorsCont[2]++;
                case GREEN -> colorsCont[3]++;
                case PINK -> colorsCont[4]++;
            }
        }

        Color[] possibleColors = Color.values();
        for(int i = 0; i < possibleColors.length; i++){
            colors.put(possibleColors[i], colorsCont[i]);
        }

        return new Report(owner, numTowers, colors, null, 0);
    }

    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }

    public void addStudent(Student s){
        students.add(s);
    }

    public Student removeStudent(int id) throws NoSuchStudentException {
        //Random color because it doesn't matter for the equals method (Each Student has a unique id)
        Student tempStudent = new Student(id, Color.BLUE);
        int positionInList = this.students.indexOf(tempStudent);

        if(positionInList == -1){
            throw new NoSuchStudentException("There isn't any Student with that id in the list");
        }

        Student removedStudent = this.students.get(positionInList);
        this.students.remove(positionInList);

        return removedStudent;
    }

    //Change the owner of the island
    public void conquest(ColorTower t) {
        owner=t;
    }

    public void setProhibition(boolean prohibitionToken) {
        this.prohibitionToken = prohibitionToken;
    }

    public boolean getProhibition(){
        return prohibitionToken;
    }


    public boolean equals(Object o){
        if(o instanceof Island){
            return ((Island) o).getId().equals(id);
        }
        return false;
    }

    public String toString(){
        //return String.valueOf(id);
        return id;
    }

    public boolean checkContainedId(int idToCheck){
        String x = String.format("%02d", idToCheck);
        String[] arr = this.id.split(", ");
        return Arrays.asList(arr).contains(x);
    }
}