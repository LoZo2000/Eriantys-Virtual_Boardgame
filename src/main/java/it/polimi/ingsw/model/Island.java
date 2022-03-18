package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.TowersColorException;
import it.polimi.ingsw.model.exceptions.WrongNumberOfTowersException;

import java.util.ArrayList;

public class Island {
    private final int id;
    private ArrayList<Student> students;
    private ArrayList<Tower> towers;
    private boolean prohibitionToken = false;
    private int maxTowers;

    public Island(int id){
        this.id = id;
        students = new ArrayList<Student>();
        towers = new ArrayList<Tower>();
        maxTowers = 1;
    }

    //Creator to merge two islands (in Game you still have to delete the 2 previous islands and move motherNature to this new created island!!!)
    //The new id will be the id of the first island (FEEL FREE TO CHANGE IT)
    public Island(Island i1, Island i2){
        this(i1.getId());
        students.addAll(i1.getAllStudents());
        students.addAll(i2.getAllStudents());
        towers.addAll(i1.getAllTowers());
        towers.addAll(i2.getAllTowers());
        maxTowers = i1.getMaxTowers() + i2.getMaxTowers();
    }

    public int getId(){
        return id;
    }
    public int getMaxTowers(){
        return maxTowers;
    }
    public ArrayList<Student> getAllStudents(){
        return (ArrayList<Student>)students.clone();
    }
    public ArrayList<Tower> getAllTowers(){
        return (ArrayList<Tower>)towers.clone();
    }

    //Returns the color of the player who currently owns the island (null if the island is still free)
    public ColorTower getCurrentOwner(){
        if(towers.size()>0) return towers.get(0).getColorTower();
        return null;
    }

    //Returns the number of students
    public int getNumberStudentColor(Color color){
        int cont = 0;
        for(Student s : students){
            if(s.getColor() == color) cont++;
        }
        return cont;
    }

    public void addStudent(Student s){
        students.add(s);
    }

    //This method will be launched only ONCE: the first time a player conquests the island
    public void conquest(Tower t) throws WrongNumberOfTowersException, TowersColorException {
        if(maxTowers==1){
            if(towers.size()==0 || towers.get(0).getColorTower()!=t.getColorTower()) {
                towers.clear();
                towers.add(t);
            }
            else throw new TowersColorException();
        }
        else throw new WrongNumberOfTowersException();
    }

    //Saves in 'towers' the new owner's towers (resetting 'towers')
    public void conquest(ArrayList<Tower> towers) throws WrongNumberOfTowersException, TowersColorException {
        if(towers.size() == maxTowers) {
            if(this.towers.get(0).getColorTower() != towers.get(0).getColorTower()) {
                for(int i=1; i<towers.size(); i++){
                    if(towers.get(i).getColorTower() != towers.get(i-1).getColorTower()){
                        throw new TowersColorException();
                    }
                }
                this.towers.clear();
                this.towers.addAll(towers);
            }
            else throw new TowersColorException();
        }
        else throw new WrongNumberOfTowersException();
    }

    public void setProhibition(boolean prohibitionToken) {
        this.prohibitionToken = prohibitionToken;
    }
    public boolean getProhibition(){
        return prohibitionToken;
    }
}