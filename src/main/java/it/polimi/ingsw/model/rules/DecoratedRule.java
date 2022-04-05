package it.polimi.ingsw.model.rules;

public abstract class DecoratedRule implements Rule{
    protected Rule defaultRules;

    public DecoratedRule(){
        this.defaultRules = new DefaultRule();
    }

}
