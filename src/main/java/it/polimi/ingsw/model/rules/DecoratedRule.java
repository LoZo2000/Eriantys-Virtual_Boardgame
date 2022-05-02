package it.polimi.ingsw.model.rules;

import java.io.Serializable;

public abstract class DecoratedRule implements Serializable, Rule{
    protected Rule defaultRules;

    public DecoratedRule(){
        this.defaultRules = new DefaultRule();
    }

}
