package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.NoActiveCardException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class GameReport implements Serializable {
    private final String turnOf;
    private final Phase currentPhase;
    private final String namePlayer;
    private final String error;
    private final int remainingMoves;
    private final int remainingExchanges;
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



    //Attributes for GUI:
    private Phase phase;
    private int numPlayers = 0;
    private ArrayList<Card> myCards = new ArrayList<>();
    private ArrayList<String> allPlayersNick = new ArrayList<>();
    private ColorTower myColor;
    private ArrayList<ColorTower> allPlayersColor = new ArrayList<>();
    private LinkedList<Island> allIslands = new LinkedList<>();
    private int MT;
    private ArrayList<ArrayList<Student>> studentsOnClouds = new ArrayList<>();
    private ArrayList<Student> myEntrance = new ArrayList<>();
    private ArrayList<ArrayList<Student>> opponentsEntrance = new ArrayList<>();
    private ArrayList<Integer> myCanteen = new ArrayList<>();
    private ArrayList<Integer> lastMyCanteen = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> opponentsCanteen = new ArrayList<>();
    private Card myLastCard;
    private ArrayList<Card> opponentsLastCard = new ArrayList<>();
    private int myTowers;
    private ArrayList<Integer> towersInDash = new ArrayList<>();
    private ArrayList<Character> charId = new ArrayList<>();
    private int myCoins;
    private ArrayList<Integer> opponentsCoins = new ArrayList<>();



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

    public GameReport(String myId, String error, String turnOf, boolean finished){
        this.namePlayer = myId;
        this.error = error;
        this.finished =finished;
        this.winner=null;
        this.turnOf = turnOf;

        this.activeRule = null;
        this.characters = null;
        this.activeCard = -1;
        this.requestedAction = null;
        this.remainingMoves = -1;
        this.remainingExchanges = -1;
        this.currentPhase = null;
        this.player = null;
        this.islands = null;
        this.opponents = null;
        this.professors = null;
        this.clouds = null;
    }


    public GameReport(Game game, Player owner){
        //System.out.println("BuildingGameRep");

        this.error = null;
        this.finished =game.getFinishedGame();
        this.winner=game.getWinner();

        islands = new ArrayList<>();
        professors = new HashMap<>();
        clouds = new ArrayList<>();
        opponents = new ArrayList<>();
        characters = new ArrayList<>();

        this.phase = game.getCurrentPhase();
        this.turnOf = game.getCurrentPlayer();
        this.namePlayer = owner.getNickname();

        //ISLANDS
        int b = 8;
        int w = 8;
        int g = 6;
        if(game.getNumPlayers()==3){
            b = 6;
            w = 6;
        }
        int numI = 0;
        allIslands = game.getAllIslands();
        for(Island i : game.getAllIslands()){
            if(i.getReport().getOwner()==ColorTower.BLACK) b -= i.getReport().getTowerNumbers();
            else if(i.getReport().getOwner()==ColorTower.WHITE) w -= i.getReport().getTowerNumbers();
            else if(i.getReport().getOwner()==ColorTower.GREY) g -= i.getReport().getTowerNumbers();

            IslandReport ir;
            Map<Color, Integer> mapNumbers = new HashMap<>();
            for(Color c : Color.values()){
                mapNumbers.put(c, i.getReport().getColorStudents(c));
            }

            if(game.getMotherNaturePosition().equals(i)){
                MT = numI;
                ir = new IslandReport(i.getId(), mapNumbers, i.getProhibition(), i.getReport().getTowerNumbers(), i.getOwner(), true);
            } else{
                ir = new IslandReport(i.getId(), mapNumbers, i.getProhibition(), i.getReport().getTowerNumbers(), i.getOwner(), false);
            }
            numI++;
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
            studentsOnClouds.add(clGame[i].getStudents());
        }

        //OTHER PLAYERS
        numPlayers = game.getNumPlayers();
        for(Player p : game.getAllPlayers()){
            allPlayersNick.add(p.getNickname());
            allPlayersColor.add(p.getColor());
            switch (p.getColor()){
                case BLACK -> towersInDash.add(b);
                case WHITE -> towersInDash.add(w);
                case GREY -> towersInDash.add(g);
            }
            opponentsEntrance.add(p.getDashboard().getEntrance().getAllStudents());
            ArrayList<Integer> prov = new ArrayList<>();
            prov.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.GREEN));
            prov.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.RED));
            prov.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.YELLOW));
            prov.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.PINK));
            prov.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.BLUE));
            opponentsCanteen.add(prov);
            opponentsLastCard.add(p.getDashboard().getGraveyard());
            opponentsCoins.add(p.getCoins());

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
        myCoins = owner.getCoins();
        myCards = owner.getHand().getAllCards();
        myEntrance = owner.getDashboard().getEntrance().getAllStudents();

        myCanteen.add(owner.getDashboard().getCanteen().getNumberStudentColor(Color.GREEN));
        if(owner.getDashboard().getCanteen().getNumberStudentColor(Color.GREEN)==0) lastMyCanteen.add(-1);
        else lastMyCanteen.add(owner.getDashboard().getCanteen().getStudents(Color.GREEN).get(0).getId());

        myCanteen.add(owner.getDashboard().getCanteen().getNumberStudentColor(Color.RED));
        if(owner.getDashboard().getCanteen().getNumberStudentColor(Color.RED)==0) lastMyCanteen.add(-1);
        else lastMyCanteen.add(owner.getDashboard().getCanteen().getStudents(Color.RED).get(0).getId());

        myCanteen.add(owner.getDashboard().getCanteen().getNumberStudentColor(Color.YELLOW));
        if(owner.getDashboard().getCanteen().getNumberStudentColor(Color.YELLOW)==0) lastMyCanteen.add(-1);
        else lastMyCanteen.add(owner.getDashboard().getCanteen().getStudents(Color.YELLOW).get(0).getId());

        myCanteen.add(owner.getDashboard().getCanteen().getNumberStudentColor(Color.PINK));
        if(owner.getDashboard().getCanteen().getNumberStudentColor(Color.PINK)==0) lastMyCanteen.add(-1);
        else lastMyCanteen.add(owner.getDashboard().getCanteen().getStudents(Color.PINK).get(0).getId());

        myCanteen.add(owner.getDashboard().getCanteen().getNumberStudentColor(Color.BLUE));
        if(owner.getDashboard().getCanteen().getNumberStudentColor(Color.BLUE)==0) lastMyCanteen.add(-1);
        else lastMyCanteen.add(owner.getDashboard().getCanteen().getStudents(Color.BLUE).get(0).getId());

        myLastCard = owner.getDashboard().getGraveyard();
        List<Card> hand = owner.getHand().getAllCards();
        List<Student> entrance = owner.getDashboard().getEntrance().getAllStudents();
        Map<Color, List<Student>> mapCanteen = new HashMap<>();
        switch (owner.getColor()){
            case BLACK:
                myTowers = b;
                myColor = ColorTower.BLACK;
                break;
            case WHITE:
                myTowers = w;
                myColor = ColorTower.WHITE;
                break;
            case GREY:
                myTowers = g;
                myColor = ColorTower.GREY;
        }

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
                charId.add(c);
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

            if(requestedAction == Action.EXCHANGESTUDENT){
                this.remainingExchanges = game.getRemainingExchanges();
            } else{
                this.remainingExchanges = -1;
            }

            activeRule = game.getCurrentRule().getClass().getSimpleName();
        } else{
            activeCard = -1;
            requestedAction = null;
            remainingExchanges = -1;
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
            if(activeCard == i){
                s.append(ansi().fgBrightBlue().a("Card " + i).fgBrightCyan().a(" ACTIVE").reset().toString()).append("\n");
            }else {
                s.append(ansi().fgBrightBlue().a("Card " + i).reset().toString()).append("\n");
            }
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

    public String getCurrentPhaseString(){
        String result = "";
        if(currentPhase == Phase.MIDDLETURN){
            result += currentPhase +" - "+remainingMoves + " Remaining Moves";
        } else{
            result += currentPhase.name();
        }

        if(remainingExchanges != -1 && (currentPhase == Phase.MIDDLETURN || currentPhase == Phase.MOVEMNTURN)){
            result += " - "+remainingExchanges + " Remaining Exchanges";
        }

        return result;
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

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public String getError(){
        return error;
    }

    public Boolean getFinishedGame(){ return this.finished;}

    public String getWinner(){ return this.winner;}



    //Getter for GUI:
    public Phase getPhase(){
        return phase;
    }
    public int getNumPlayers() {
        return numPlayers;
    }
    public ArrayList<Card> getMyCards(){
        return myCards;
    }
    public ArrayList<String> getOpponentsNick(){
        return allPlayersNick;
    }
    public ColorTower getMyColor(){
        return myColor;
    }
    public ArrayList<ColorTower> getAllPlayersColor(){
        return allPlayersColor;
    }
    public LinkedList<Island> getAllIslands(){
        return allIslands;
    }
    public int getMT(){
        return MT;
    }
    public ArrayList<ArrayList<Student>> getStudentsOnClouds(){
        return studentsOnClouds;
    }
    public ArrayList<Student> getMyEntrance(){
        return myEntrance;
    }
    public ArrayList<ArrayList<Student>> getOpponentsEntrance(){
        return opponentsEntrance;
    }
    public ArrayList<Integer> getMyCanteen(){
        return myCanteen;
    }
    public ArrayList<ArrayList<Integer>> getOpponentsCanteen(){
        return opponentsCanteen;
    }
    public Card getMyLastCard(){
        return myLastCard;
    }
    public ArrayList<Card> getOpponentsLastCard(){
        return opponentsLastCard;
    }

    public int getMyTowers(){
        return myTowers;
    }
    public ArrayList<Integer> getTowersInDash(){
        return towersInDash;
    }
    public Map<Color, String> getProfessors(){
        return professors;
    }
    public ArrayList<Character> getChar(){
        return charId;
    }
    public int getMyCoins(){
        return myCoins;
    }
    public ArrayList<Integer> getOpponentsCoins(){
        return opponentsCoins;
    }
    public int getRemainingMoves(){
        return remainingMoves;
    }
    public ArrayList<Integer> getLastMyCanteen(){
        return lastMyCanteen;
    }
}