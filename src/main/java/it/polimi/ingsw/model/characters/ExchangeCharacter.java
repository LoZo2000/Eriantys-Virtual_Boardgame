package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.rules.ActionRule;
import it.polimi.ingsw.model.rules.ExchangeRule;
import it.polimi.ingsw.model.rules.Rule;

import java.util.ArrayList;
import java.util.Set;

import static org.fusesource.jansi.Ansi.ansi;

public class ExchangeCharacter extends MovementCharacter implements Movable {

    private final int maxMoves;

    public ExchangeCharacter(int id, CharacterType type, String desc, int cost, ArrayList<Student> students, JSONParams params){
        super(id, type, desc, cost, students, params);

        maxMoves = params.getMaxMoves();
    }

    public int getMaxMoves(){
        return maxMoves;
    }

    @Override
    public Rule usePower(Player player) throws NotEnoughMoneyException, IllegalMoveException {
        if(allowedArrivals.contains(Location.CANTEEN)){
            if(checkEmptyCanteen(player)){
                throw new IllegalMoveException("You can't exchange students with the canteen if there isn't any student in it.");
            }
        }
        player.useCoins(this.getCost());
        this.increaseTimesUsed();

        return new ExchangeRule(maxMoves);
    }

    @Override
    public String shortString(){
        String s = super.shortString();
        s += ansi().a(" - ").bold().a("Max Exchanges: ").reset().a(maxMoves).toString();
        return s;
    }

    @Override
    public String toString(){
        String s = super.toString();
        s += "\u001B[1mMaximum Exchanges:\u001B[0m " + this.maxMoves + "\n";
        return s;
    }

    private boolean checkEmptyCanteen(Player p) {
        for (Color c : Color.values()) {
            if (p.getDashboard().getCanteen().getNumberStudentColor(c) != 0)
                return false;
        }

        return true;
    }
}
