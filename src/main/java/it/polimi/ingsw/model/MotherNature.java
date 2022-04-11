package it.polimi.ingsw.model;

import java.io.Serializable;

public class MotherNature implements Serializable {
    private Island position;

    public MotherNature(){position = null;}
    public MotherNature(Island position){
        this.position = position;
    }

    public void movement(Island arrival){
        position = arrival;
    }

    //Remember: this method returns the island on which motherNature is, not a copied object!!!
    public Island getPosition(){
        return position;
    }
}