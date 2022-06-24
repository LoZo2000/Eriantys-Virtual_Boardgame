package it.polimi.ingsw.model.rules;

import java.io.Serializable;

/**
 * The class DecoratedRule is inherited by all the Rules that are not the DefaultRules and that modify the behaviour of some
 * aspects of the game, in particular reimplement (override) the methods of the interface Rule
 */
public abstract class DecoratedRule implements Serializable, Rule{
    protected Rule defaultRules;

    /**
     * This method is the constructor of the class
     */
    public DecoratedRule(){
        this.defaultRules = new DefaultRule();
    }

}