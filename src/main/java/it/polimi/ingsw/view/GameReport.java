package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;

import static org.fusesource.jansi.Ansi.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameReport implements Serializable {
    private final String turnOf;
    private final Phase currentPhase;
    private final String namePlayer;
    private final String error;
    private final int remainingMoves;
    private final int activeCard;
    private final Action requestedAction;
    private final String activeRule;
    private final Boolean finished;
    private final String winner;

    private final List<IslandReport> islands;
    private final List<CloudReport> clouds;
    private final PlayerReport player;
    private final List<OpponentReport> opponents;
    private final List<CharacterReport> characters;
    private final Map<Color, String> professors;

    private record IslandReport (String id, Map<Color, Integer> students, boolean prohibitionToken, int numTowers, ColorTower owner, boolean motherNature) implements Serializable{
        public Map<Color, Integer> students(){
            Map<Color, Integer> newMap = new HashMap<>();
            for(Color c : Color.values()){
                newMap.put(c, students.get(c));
            }
            return newMap;
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
            s.append("Id: ").append(id).append("    ");
            if(owner == null)
                s.append("Owner: nobody  ");
            else
                s.append("Owner: ").append(owner).append("    ");
            s.append("Towers: x").append(numTowers).append("    ");

            for(Color c : Color.values()){
                s.append(ansi().fgBright(c.ansiColor).a("(" + c.shortName + ":" + students.get(c) + ")").reset().a("   ").toString());
            }

            if(motherNature)
                s.append("MN");
            if(prohibitionToken)
                s.append(ansi().fgRed().a("BLOCK").reset().toString());

            return s.toString();
        }
    }

    private record CloudReport (int id, Map<Color, Integer> students, boolean isFull) implements Serializable{
        public Map<Color, Integer> students(){
            Map<Color, Integer> newMap = new HashMap<>();
            for(Color c : Color.values()){
                newMap.put(c, students.get(c));
            }
            return newMap;
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
            s.append("Cloud ").append(id).append("  =>   ");
            if(!isFull)
                s.append("Already chosen cloud");
            else {
                for(Color c : Color.values()){
                    if (students.get(c) != 0)
                        s.append(ansi().fgBright(c.ansiColor).a(c + ": " + students.get(c)).reset().a("   ").toString());
                }
            }
            return s.toString();
        }
    }

    private record OpponentReport(String nickname, ColorTower color, Card lastCard, Map<Color, Integer> studentsEntrance, Map<Color, Integer> studentsCanteen, int remainingTowers, int coins) implements Serializable{
        public Map<Color, Integer> studentsEntrance(){
            Map<Color, Integer> newMap = new HashMap<>();
            for(Color c : Color.values()){
                newMap.put(c, studentsEntrance.get(c));
            }
            return newMap;
        }

        public Map<Color, Integer> studentsCanteen(){
            Map<Color, Integer> newMap = new HashMap<>();
            for(Color c : Color.values()){
                newMap.put(c, studentsCanteen.get(c));
            }
            return newMap;
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
            s.append(ansi().bold().fgCyan().a("DASHBOARD of " + nickname).reset().a("  (Color: " + color + ")\n").toString());

            s.append(ansi().bold().a("Last Card: ").reset().toString()).append(lastCard).append("   ");
            s.append(ansi().bold().a("Towers: ").reset().toString()).append(remainingTowers).append("   ");

            if(coins != -1){
                s.append(ansi().bold().a("Coins: ").reset().toString()).append(coins).append("\n");
            } else{
                s.append("\n");
            }

            s.append(ansi().bold().a("Entrance:  ").reset().toString());
            for(Color c : Color.values()){
                s.append(ansi().fgBright(c.ansiColor).a(c + ": " + studentsEntrance.get(c)).reset().a("  ").toString());
            }

            s.append("\n");

            s.append(ansi().bold().a("Canteen:  ").reset().toString());
            for(Color c : Color.values()){
                s.append(ansi().fgBright(c.ansiColor).a(c + ": " + studentsCanteen.get(c) + "/10").reset().a("  ").toString());
            }

            return s.toString();
        }
    }

    private record PlayerReport(ColorTower color, Card lastCard, int remainingTowers, List<Card> hand, List<Student> entranceStudents, Map<Color, List<Student>> canteenStudents, int coins) implements Serializable{
        public List<Student> entranceStudents(){
            return new ArrayList<>(entranceStudents);
        }

        public Map<Color, List<Student>> canteenStudents(){
            Map<Color, List<Student>> newMap = new HashMap<>();
            for(Color c : Color.values()){
                List<Student> newList = new ArrayList<>(entranceStudents);
                newMap.put(c, newList);
            }
            return newMap;
        }

        @Override
        public String toString(){
            StringBuilder s = new StringBuilder();
            s.append(ansi().bold().fgBrightBlue().a("YOUR DASHBOARD").reset().a("  (Color: " + color + ")\n").toString());

            s.append(ansi().bold().a("Last Card: ").reset().toString()).append(lastCard).append("   ");
            s.append(ansi().bold().a("Towers: ").reset().toString()).append(remainingTowers).append("   ");

            if(coins != -1){
                s.append(ansi().bold().a("Coins: ").reset().toString()).append(coins).append("\n");
            } else{
                s.append("\n");
            }

            s.append(ansi().bold().a("Your hand:  ").reset().toString());

            for(Card c : hand){
                s.append(c).append(" ");
            }

            s.append("\n");

            s.append(ansi().bold().a("Entrance:  ").reset().a(entranceStudents).toString());

            s.append("\n");

            s.append(ansi().bold().a("Canteen:").reset().a("\n").toString());
            for(Color c : Color.values()){
                s.append(ansi().fgBright(c.ansiColor).a("   " + c + ": ").reset().a(canteenStudents.get(c)).a("\n").toString());
            }

            return s.toString();
        }
    }

    private record CharacterReport(Character c) implements Serializable{
        public String shortString(){
            return c.shortString();
        }

        @Override
        public String toString(){
            return c.toString();
        }
    }

    public GameReport(String myId, String error, String turnOf){
        this.namePlayer = myId;
        this.error = error;
        this.finished =false;
        this.winner=null;
        this.turnOf = turnOf;

        this.activeRule = null;
        this.characters = null;
        this.activeCard = -1;
        this.requestedAction = null;
        this.remainingMoves = -1;
        this.currentPhase = null;
        this.player = null;
        this.islands = null;
        this.opponents = null;
        this.professors = null;
        this.clouds = null;
    }


    public GameReport(Game game, Player owner){
        this.error = null;
        this.finished =game.getFinishedGame();
        this.winner=game.getWinner();

        islands = new ArrayList<>();
        professors = new HashMap<>();
        clouds = new ArrayList<>();
        opponents = new ArrayList<>();
        characters = new ArrayList<>();

        this.turnOf = game.getCurrentPlayer();
        this.namePlayer = owner.getNickname();

        //ISLANDS
        for(Island i : game.getAllIslands()){
            IslandReport ir;
            Map<Color, Integer> mapNumbers = new HashMap<>();
            for(Color c : Color.values()){
                mapNumbers.put(c, i.getReport().getColorStudents(c));
            }

            if(game.getMotherNaturePosition().equals(i)){
                ir = new IslandReport(i.getId(), mapNumbers, i.getProhibition(), i.getReport().getTowerNumbers(), i.getOwner(), true);
            } else{
                ir = new IslandReport(i.getId(), mapNumbers, i.getProhibition(), i.getReport().getTowerNumbers(), i.getOwner(), false);
            }

            islands.add(ir);
        }

        //PROFESSORS
        for(Color c : Color.values()){
            if(game.getProfessors().get(c) != null)
                professors.put(c, game.getProfessors().get(c).getNickname());
            else
                professors.put(c, "nobody");
        }

        //CLOUDS
        Cloud[] clGame = game.getAllClouds();
        for(int i = 0; i<clGame.length; i++){
            Map<Color, Integer> mapNumbers = new HashMap<>();
            for(Color c : Color.values()){
                mapNumbers.put(c, clGame[i].getNumberOfStudentPerColor(c));
            }
            clouds.add(new CloudReport(i, mapNumbers, clGame[i].isFull()));
        }

        //OTHER PLAYERS
        for(Player p : game.getAllPlayers()){
            if(!p.equals(owner)){
                List<Student> entranceStudents = p.getDashboard().getEntrance().getAllStudents();

                Map<Color, Integer> mapEntrance = new HashMap<>();
                for(Color c : Color.values()){
                    mapEntrance.put(c, 0);
                }
                for(Student s : entranceStudents){
                    mapEntrance.put(s.getColor(), mapEntrance.get(s.getColor()) + 1);
                }

                Map<Color, Integer> mapCanteen = new HashMap<>();
                for(Color c : Color.values()){
                    mapCanteen.put(c, p.getDashboard().getCanteen().getNumberStudentColor(c));
                }

                if(game.getCompleteRules())
                    opponents.add(new OpponentReport(p.getNickname(), p.getColor(), p.getDashboard().getGraveyard(), mapEntrance, mapCanteen, p.getDashboard().getTowers(), p.getCoins()));
                else
                    opponents.add(new OpponentReport(p.getNickname(), p.getColor(), p.getDashboard().getGraveyard(), mapEntrance, mapCanteen, p.getDashboard().getTowers(), -1));
            }
        }

        //PLAYER DASHBOARD
        List<Card> hand = owner.getHand().getAllCards();
        List<Student> entrance = owner.getDashboard().getEntrance().getAllStudents();
        Map<Color, List<Student>> mapCanteen = new HashMap<>();

        for(Color c : Color.values()){
            mapCanteen.put(c, owner.getDashboard().getCanteen().getStudents(c));
        }

        if(game.getCompleteRules())
            player = new PlayerReport(owner.getColor(), owner.getDashboard().getGraveyard(), owner.getDashboard().getTowers(), hand, entrance, mapCanteen, owner.getCoins());
        else
            player = new PlayerReport(owner.getColor(), owner.getDashboard().getGraveyard(), owner.getDashboard().getTowers(), hand, entrance, mapCanteen, -1);

        remainingMoves = game.getRemainingMoves();
        currentPhase = game.getCurrentPhase();

        if(game.getCompleteRules()){
            //CHARACTERS
            for(Character c : game.getCharactersCards()){
                characters.add(new CharacterReport(c));
            }

            //ACTIVE CARD
            activeCard = game.getActiveCard();
            if(activeCard != -1){
                try {
                    requestedAction = game.getRequestedAction();
                } catch (NoActiveCardException e) {
                    throw new RuntimeException(e);
                }
            } else{
                requestedAction = null;
            }

            activeRule = game.getCurrentRule().getClass().getSimpleName();
        } else{
            activeCard = -1;
            requestedAction = null;
            activeRule = null;
        }
    }

    public String getIslandsString(){
        StringBuilder s = new StringBuilder();
        int i = 0;
        for(IslandReport ir : islands){
            int color = 31+i;
            s.append("\u001B[").append(color).append("m||\u001B[0m ").append(ir.toString()).append("\n");
            i = (i+1) % 7;
        }
        return s.toString();
    }

    public String getProfessorsString(){
        StringBuilder s = new StringBuilder();
        for(Color c : Color.values()){
            s.append(ansi().fgBright(c.ansiColor).a(c + ": " + professors.get(c)).reset().a("    ").toString());
        }
        s.append("\n");

        return s.toString();
    }

    public String getCloudsString(){
        StringBuilder s = new StringBuilder();
        for(CloudReport cr : clouds){
            s.append(cr.toString()).append("\n");
        }
        return s.toString();
    }

    public String getOpponentsString(){
        StringBuilder s = new StringBuilder();
        for(OpponentReport or : opponents){
            s.append(or.toString()).append("\n");
        }
        return s.toString();
    }

    public String getShortCharacters(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < characters.size(); i++){
            s.append(ansi().fgBrightBlue().a("Card " + i).reset().toString()).append("\n");
            s.append(characters.get(i).shortString()).append("\n\n");
        }
        return s.toString();
    }

    public String getCharacters(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < characters.size(); i++){
            s.append(ansi().fgBrightBlue().a("Card " + i).reset().toString()).append("\n");
            s.append(characters.get(i).toString()).append("\n\n");
        }
        return s.toString();
    }

    public String getYourDashboardString(){
        return player.toString();
    }

    public String getTurnOf(){
        return turnOf;
    }

    public String getNamePlayer(){
        return namePlayer;
    }

    public String getCurrentPhase(){
        if(currentPhase == Phase.MIDDLETURN){
            return currentPhase +" - "+remainingMoves + " Remaining Moves";
        } else{
            return currentPhase.name();
        }
    }

    public int getActiveCard(){
        return activeCard;
    }

    public Action getRequestedAction(){
        return requestedAction;
    }

    public String getActiveRule(){
        return activeRule;
    }

    public String getError(){
        return error;
    }

    public Boolean getFinishedGame(){ return this.finished;}

    public String getWinner(){ return this.winner;}

}
