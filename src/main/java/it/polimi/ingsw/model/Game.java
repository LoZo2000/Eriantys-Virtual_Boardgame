package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.messages.GameStatus;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.rules.DefaultRule;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.Rule;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.view.Columns;
import it.polimi.ingsw.view.GameReport;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Game extends Observable<GameReport> {
    private MotherNature motherNature;
    private LinkedList <Island> islands = new LinkedList<>();
    private Cloud[] clouds;
    //private Team[] teams;
    private ArrayList<Player> players = new ArrayList<>();
    private Bag bag;
    private Map<Color, Player> professors;
    private Rule currentRule;
    private final int numPlayers;
    private final boolean completeRules;
    private int activeCard;
    private Character[] charactersCards;

    private final String JSON_PATH = "characters.json";
    private String currentPlayer = null;
    private Phase currentPhase = Phase.PREGAME;
    private Map<Player, Card> playedCards;
    private int remainingMoves;
    private boolean usedCard;
    private boolean finishedGame = false;
    private String winner;

    //Create game but no players are added;
    public Game(boolean completeRules, int numPlayers){
        this.numPlayers=numPlayers;
        this.completeRules=completeRules;

        //Init Bag
        FactoryBag fb = new FactoryBag();
        bag = fb.getBag();

        //Init islands and mother nature
        Bag initBag = fb.getInitBag();
        initIslands(initBag);
        motherNature = new MotherNature();
        motherNature.movement(islands.get(0));
        try{
            initClouds(numPlayers);
        } catch (Exception e ) {
            e.printStackTrace();
        }

        //PLAYED CARDS
        this.playedCards = new Hashtable<>();

        //RULES
        this.currentRule = new DefaultRule();

        //PROFESSORS
        this.professors = new HashMap<>();
        for(Color c: Color.values()){
            this.professors.put(c, null);
        }

        //CHARACTERS (Only complete rules)

        this.usedCard = false;

        if(this.completeRules){
            this.activeCard = -1;
            try {
                this.charactersCards = initCardsFromJSON();
            }catch (IOException e){
                e.printStackTrace();
            }
            //Arrays.stream(charactersCards).map(Character::toString)
            //        .forEach(System.out::println);
        } else{
            this.activeCard = -1;
            this.charactersCards = null;
        }
    }

    public int getNumberOfStudentPerColorOnCloud(int i, Color color){
        return clouds[i].getNumberOfStudentPerColor(color);
    }

    public int getNumberOfClouds(){
        return clouds.length;
    }

    public void endGame(ColorTower winner){
        finishedGame=true;
        for(Player p : players){
            if(p.getColor()==winner){
                this.winner=p.getNickname();
            }
        }
    }

    private void initClouds(int numPlayers) throws NoMoreStudentsException, TooManyStudentsException, StillStudentException {
        switch(numPlayers){
            case 2:
            case 4:
                clouds= new Cloud[numPlayers];
                for(int i=0; i<numPlayers; i++){
                    clouds[i]= new Cloud(3);
                    clouds[i].refillCloud(bag.getRandomStudent(3));
                }
                break;
            case 3:
                clouds= new Cloud[numPlayers];
                for(int i=0; i<numPlayers; i++){
                    clouds[i]= new Cloud(4);
                    clouds[i].refillCloud(bag.getRandomStudent(4));
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value of Players: " + numPlayers);
        }
    }

    private void initIslands(Bag initBag){
        for(int i=0; i<12; i++){
            islands.add(new Island(i));
            if(i!=0 && i!=6) {
                try {
                    islands.get(i).addStudent(initBag.getRandomStudent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //TODO TEMPORARY METHOD
    public void addPlayer(Player p){
        this.players.add(p);

        //AddPlayer has worked: update all the clients!
        //for(Player pl : getAllPlayers()) notify(getGameStatus(pl.getNickname()));
    }

    //TODO TEMPORARY METHOD
    public Map<Color, Player> getProfessors(){
        return this.professors;
    }

    public void addPlayer(String nickname, ColorTower color){
        int numberOfStudents = this.numPlayers != 3 ? 7 : 9;
        ArrayList<Student> entranceStudents = extractFromBag(numberOfStudents);

        Player newPlayer = new Player(nickname, numPlayers, color, entranceStudents);

        players.add(newPlayer);

        if(players.size() < numPlayers){
            sendErrorNote(nickname, "Waiting for other players...", null);
        }

        //AddPlayer has worked: update all the clients!
        //for(Player pl : getAllPlayers()) notify(getGameStatus(pl.getNickname()));
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public int getRegisteredNumPlayers(){
        return this.players.size();
    }

    public LinkedList<Island> getAllIslands(){
        return (LinkedList<Island>)islands.clone();
    }

    public Island getMotherNaturePosition(){
        return motherNature.getPosition();
    }

    /*public Island getIsland(int id) throws NoIslandException{
        for(Island i : islands){
            if(i.getId() == id) return i;
        }
        throw new NoIslandException();
    }*/
    public Island getIsland(int id) throws NoIslandException {
        for(Island i : islands){
            //if(i.getId() == id) return i;
            if(i.checkContainedId(id)) return i;
        }
        throw new NoIslandException();
    }

    public Player getPlayer(String nickName) throws NoPlayerException{
        for(Player p : players){
            if(p.getNickname().equals(nickName)) return p;
        }
        throw new NoPlayerException();
    }

    public ArrayList<Player> getAllPlayers(){
        return (ArrayList<Player>) players.clone();
    }

    public void playCard(Player player, int priority) throws OverflowCardException, AlreadyPlayedCardException {
        Hand h = player.getHand();

        List<Card> cards = h.getAllCards();
        if(!playedCards.values().containsAll(cards)){
            //Card card = cards.get(priority-1);
            Card card = new Card(priority, priority/2+priority%2);
            if(playedCards.containsValue(card)){
                throw new AlreadyPlayedCardException("Another player already played this card");
            }
        }

        Card c = h.playCard(priority);
        playedCards.put(player, c);
        player.getDashboard().setGraveyard(c);

        //PlayCard has worked: update all the clients!
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
    }

    public void resetPlayedCards(){
        playedCards = new Hashtable<>();;
    }

    public int getMaximumMNMovement(Player p){
        return playedCards.get(p).getMovement();
    }

    //When parameter enableMovement is true the method has the regular behaviour.
    //If enableMovement is false, it will be checked if there's an active card.
    //If not an Exception will be raised
    public void moveMotherNature(Island island, boolean enableMovement) throws NoActiveCardException, EndGameException, NoMoreTokensException {
        if(enableMovement)
            motherNature.movement(island);
        else{
            if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

            ActionCharacter ac = (ActionCharacter) this.charactersCards[this.activeCard];
            if(ac.getType() != Action.ISLAND_INFLUENCE)
                throw new NoActiveCardException("You can't calculate the influence of an island with this power");

            //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
            //}
        }
        Report report = island.getReport();

        if(!island.getProhibition()) {
            ColorTower higherInfluence = influence(report);

            if (higherInfluence != report.getOwner()) {
                island.conquest(higherInfluence);
                //TODO Necesario passare per Team
                for (Player p : this.players) {
                    if (p.getColor() == report.getOwner()) {
                        p.getDashboard().addTowers(report.getTowerNumbers());
                    }
                    if (p.getColor() == higherInfluence) {
                        p.getDashboard().removeTowers(report.getTowerNumbers());
                        if(p.getDashboard().getTowers()==0){
                            throw new EndGameException();
                        }
                    }
                }

                int islandNumber = this.islands.indexOf(island);
                int previousPosition = (islandNumber - 1) % this.islands.size();
                if (previousPosition < 0) previousPosition += this.islands.size();

                if (this.islands.get(previousPosition).getOwner() == island.getOwner()) {
                    if(this.islands.get(previousPosition).getProhibition() && island.getProhibition()){
                        addProhibitionToken(island);
                    }
                    island = mergeIsland(this.islands.get(previousPosition), island);
                }

                islandNumber = this.islands.indexOf(island);
                int successivePosition = (islandNumber + 1) % this.islands.size();

                if (this.islands.get(successivePosition).getOwner() == island.getOwner()) {
                    if(this.islands.get(successivePosition).getProhibition() && island.getProhibition()){
                        addProhibitionToken(island);
                    }
                    island = mergeIsland(island, this.islands.get(successivePosition));
                }
            }
        } else{
            addProhibitionToken(island);
        }

        //MoveMotherNature has worked: update all the clients!
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
    }

    public Island mergeIsland(Island i1, Island i2) throws EndGameException {
        Island temp;
        int indx1=islands.indexOf(i1);
        int indx2=islands.indexOf(i2);
        if(indx1>indx2){
            temp= new Island(i2, i1);
            islands.remove(i1);
            islands.remove(i2);
            islands.add(indx2, temp);
        }
        else{
            temp= new Island(i1, i2);
            islands.remove(i1);
            islands.remove(i2);
            islands.add(indx1, temp);
        }
        motherNature.movement(temp);
        if(islands.size()<=3){
            throw new EndGameException();
        }
        return temp;
    }

    private ColorTower influence(Report report){
        //TODO Costruzione Mappa Professori da quella con Player

        //Map with the information about the Color of the professor and the Color of the Tower of the player or team who
        //has the professor
        HashMap<Color, ColorTower> profAndPlayer = new HashMap<>();
        for(Color c: Color.values()){
            Player owner = this.professors.get(c);
            if(owner == null)
                profAndPlayer.put(c, null);
            else
                profAndPlayer.put(c, owner.getColor());
        }

        return this.currentRule.calculateInfluence(report, profAndPlayer);
    }

    private void updateProfessors(){
        for(Color c: Color.values()){
            HashMap<String, Integer> counterCanteen = new HashMap<>();
            for(Player p: this.players){
                int numberStudents = p.getDashboard().getCanteen().getNumberStudentColor(c);
                counterCanteen.put(p.getNickname(), numberStudents);
            }
            Player currentOwner = this.professors.get(c);

            String currentOwnerNickname;
            if(currentOwner == null){
                currentOwnerNickname = null;
            } else{
                currentOwnerNickname = currentOwner.getNickname();
            }

            String newOwnerNickname = this.currentRule.updateProfessor(currentOwnerNickname, counterCanteen);

            Player newOwner = this.players.stream()
                    .filter(player -> player.getNickname().equals(newOwnerNickname))
                    .findAny()
                    .orElse(null);

            this.professors.put(c, newOwner);
        }
    }

    //MOVE FUNCTIONS
    //function move written in a view where the parameters are message received by a client (temporary)
    public void moveStudent(int studentId, Movable arrival, Movable departure) throws NoSuchStudentException, CannotAddStudentException {
        Student s = departure.removeStudent(studentId);
        arrival.addStudent(s);

        this.updateProfessors();

        if(completeRules){
            Color c = s.getColor();
            this.assignCoins(c, true, false);
        }

        //MoveStudent has worked: update all the clients!
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
    }

    public void exchangeStudent(int studentId1, int studentId2, Movable arrival, Movable departure) throws NoSuchStudentException, CannotAddStudentException {
        Student s1, s2;
        s1 = departure.removeStudent(studentId1);
        s2 = arrival.removeStudent(studentId2);

        arrival.addStudent(s1);
        departure.addStudent(s2);

        this.updateProfessors();

        //if(completeRules){
            Color c1 = s1.getColor();
            Color c2 = s2.getColor();

            this.assignCoins(c1, false, c1 == c2);
            this.assignCoins(c2, true, c1 == c2);
        //}

        //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        //}
    }

    public void refillClouds() throws NoMoreStudentsException {
        for (int i=0; i<clouds.length; i++){
            if (numPlayers==3) clouds[i].refillCloud(bag.getRandomStudent(4));
            else clouds[i].refillCloud(bag.getRandomStudent(3));
        }
    }

    public void selectCloud(String playerNick, Cloud cloud) throws NoPlayerException{
        ArrayList<Student> students = cloud.chooseCloud();
        for (Student s : students) getPlayer(playerNick).getDashboard().getEntrance().addStudent(s);

        //This will always be the last action of each player per round, so the rules can be reset now
        this.activeCard = -1;
        this.currentRule = new DefaultRule();

        //SelectCloud has worked: update all the clients!
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
    }

    public Cloud[] getAllClouds(){
        return clouds;
    }

    public void setCurrentPlayer(String nickname, boolean sendNotify){
        currentPlayer = nickname;
        if(sendNotify)
            sendNotifyAll();
    }
    public void setCurrentPhase(Phase phase){
        currentPhase = phase;
        sendNotifyAll();
    }
    public String getCurrentPlayer(){
        return currentPlayer;
    }
    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public void resetRemainingMoves(){
        this.remainingMoves = this.numPlayers==3 ? 4 : 3;
    }
    public void reduceRemainingMoves(){
        this.remainingMoves--;
        if(remainingMoves != 0)
            sendNotifyAll();
    }
    public int getRemainingMoves(){
        return remainingMoves;
    }

    //-------------------------------------------------------------------------------------
    //|                                     CHARACTERS                                    |
    //-------------------------------------------------------------------------------------

    public Character[] initCardsFromJSON() throws IOException{
        Reader reader = new FileReader(JSON_PATH);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        JSONCharacter[] jsonCharacters = gson.fromJson(reader, JSONCharacter[].class);
        List<Character> allCharacters = new ArrayList<>();
        for (JSONCharacter jc : jsonCharacters) {
            switch (jc.getTypeCharacter()) {
                case MOVEMENT:
                    ArrayList<Student> s = extractFromBag(jc.getParams().getNumThingOnIt());
                    allCharacters.add(new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), s, jc.getParams()));
                    break;

                case INFLUENCE:
                    allCharacters.add(new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;

                case PROFESSOR:
                    allCharacters.add(new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost()));
                    break;

                case MOTHERNATURE:
                    allCharacters.add(new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;

                case ACTION:
                    allCharacters.add(new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getCost(), jc.getParams()));
                    break;
            }
        }

        Character[] selectedCharacter = new Character[3];
        for(int i=0; i<3; i++){
            Random rand = new Random();
            int randomPos = rand.nextInt(allCharacters.size());
            selectedCharacter[i] = allCharacters.get(randomPos);
            allCharacters.remove(randomPos);
        }

        return selectedCharacter;
    }

    //TODO Refill method for cards which need it (Wait for controller) or use a flag in moveStudent (The Card will always be the departure Movable)

    //SameColors is a flag that checks if this movement was an exchange between 2 students of the same color.
    //In this case the Player won't receive any coin because this action could lead to a farm of coins.
    private void assignCoins(Color c, boolean reset, boolean sameColors){
        for(Player p : this.players){
            if(p.getDashboard().getCanteen().canGetCoin(c, reset)){
                if(!sameColors)
                    p.giveCoin();
            }
        }
    }

    public int getActiveCard(){
        return this.activeCard;
    }

    public Character[] getCharactersCards(){
        return this.charactersCards.clone();
    }

    //TODO Debug Method
    public Rule getCurrentRule(){
        return this.currentRule;
    }

    //TODO Debug Method
    public void setCharactersCards(Character[] characters){
        this.charactersCards = characters.clone();
    }

    public boolean getUsedCard(){
        return this.usedCard;
    }
    public void setUsedCard(boolean usedCard){
        this.usedCard = usedCard;
    }

    public boolean usePower(Player activePlayer, int card) throws NoCharacterSelectedException, NotEnoughMoneyException, IllegalMoveException {
        if(!completeRules)
            throw new IllegalMoveException("You can't use characters with simple rules!");
        if(card <= -1 || card > 2){
            throw new NoCharacterSelectedException("No character selected to use its power");
        }

        Character c = this.charactersCards[card];
        this.currentRule = c.usePower(activePlayer);

        //Case professor character
        this.updateProfessors();

        if(this.currentRule.isActionNeeded()){
            this.activeCard = card;
        } else{
            sendNotifyAll();
        }

        return this.currentRule.isActionNeeded();
    }

    public Action getRequestedAction() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        ActionCharacter c1 = (ActionCharacter) this.charactersCards[activeCard];

        return c1.getType();
    }

    public Set<Location> getAllowedDepartures() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter c1 = (MovementCharacter) this.charactersCards[activeCard];

        return c1.getAllowedDepartures();
    }

    public Set<Location> getAllowedArrivals() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter c1 = (MovementCharacter) this.charactersCards[activeCard];

        return c1.getAllowedArrivals();
    }

    public void disableIsland(Island i) throws NoActiveCardException, NoMoreTokensException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        ActionCharacter ac = (ActionCharacter) this.charactersCards[this.activeCard];
        if(ac.getType() != Action.BLOCK_ISLAND)
            throw new NoActiveCardException("You can't block an island with this power");

        ac.removeToken();

        i.setProhibition(true);

        //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        //}
    }

    public void disableColor(Player player, Color c) throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        ActionCharacter ac = (ActionCharacter) this.charactersCards[this.activeCard];
        if(ac.getType() != Action.BLOCK_COLOR)
            throw new NoActiveCardException("You can't block a color with this power");

        this.currentRule = new InfluenceRule(player.getColor(), c, 0, false);
        this.activeCard = -1;
    }

    private void addProhibitionToken(Island island) throws NoMoreTokensException{
        ActionCharacter ac;
        for(Character c : this.charactersCards){
            if(c.getTypeCharacter() == CharacterType.ACTION){
                ac = (ActionCharacter) c;
                if(ac.getType() == Action.BLOCK_ISLAND){
                    island.setProhibition(false);
                    ac.addToken();
                }
            }
        }
    }

    public void putBackInBag(Color color) throws NoActiveCardException, NoSuchStudentException {
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        ActionCharacter ac = (ActionCharacter) this.charactersCards[this.activeCard];
        if(ac.getType() != Action.PUT_BACK)
            throw new NoActiveCardException("You can't put back students with this power");

        for(Player p : this.players){
            Canteen canteen = p.getDashboard().getCanteen();
            ArrayList<Student> students = canteen.getStudents(color);

            for(int i=0; i<3 && students.size()!=0; i++){
                Student s = students.remove(0);
                canteen.removeStudent(s.getId());

                //Needed to reset the removedColor (If I try to put the same color after this method I could not get the coin)
                canteen.canGetCoin(color, true);
                this.bag.putBackStudent(s);
            }
        }

        //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        //}
    }

    public int getMotherNatureExtraMovement(){
        return this.currentRule.getMotherNatureExtraMovement();
    }

    public boolean needsRefill(){
        if(this.activeCard == -1) return false;

        MovementCharacter mc = null;
        try{
            mc = (MovementCharacter) this.charactersCards[activeCard];
        } catch(ClassCastException ex){
            return false;
        }

        /*if(!mc.isRefill()){
            if(this.activeCard != -1){
                this.activeCard = -1;
                this.currentRule = new DefaultRule();
            }
        }*/

        return mc.isRefill();
    }

    public void refillActiveCard() throws NoActiveCardException, NoMoreStudentsException, CannotAddStudentException {
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter mc = (MovementCharacter) this.charactersCards[activeCard];
        mc.addStudent(bag.getRandomStudent());

        //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        //}
    }

    /*private GameStatus getGameStatus(String owner) {
        try {

            ColorTower myColor = getPlayer(owner).getColor();
            int g = 0, b = 0, w = 0;
            List<String> islandsId = new ArrayList<>();
            List<ColorTower> owners = new ArrayList<>();
            List<Integer> numTowers = new ArrayList<>();
            List<List<Integer>> studentsOnIsland = new ArrayList<>();
            int MN=0;
            List<String> professorsOwners = new ArrayList<>();
            List<List<Integer>> studentsOnCloud = new ArrayList<>();
            List<Card> enemiesLastCards = new ArrayList<>();



            int pos=0;
            for (Island i : islands) {
                islandsId.add(i.getId());
                owners.add(i.getOwner());
                numTowers.add(i.getReport().getTowerNumbers());
                if (i.getOwner()==ColorTower.GREY) g += i.getReport().getTowerNumbers();
                else if (i.getOwner()==ColorTower.BLACK) b += i.getReport().getTowerNumbers();
                else if (i.getOwner()==ColorTower.WHITE) w += i.getReport().getTowerNumbers();
                int[] colors = {0, 0, 0, 0, 0};
                for (Student s : i.getAllStudents()) {
                    switch (s.getColor()) {
                        case BLUE -> colors[0]++;
                        case YELLOW -> colors[1]++;
                        case RED -> colors[2]++;
                        case GREEN -> colors[3]++;
                        case PINK -> colors[4]++;
                    }
                }
                List<Integer> listTemp = new ArrayList<>();
                for (int cont = 0; cont < 5; cont++) listTemp.add(colors[cont]);
                studentsOnIsland.add(listTemp);
                if(motherNature.getPosition().equals(i)) MN = pos;
                pos++;
            }

            for (Color co : Color.values()){
                if(professors.get(co)==null) professorsOwners.add("nobody");
                else professorsOwners.add(professors.get(co).getNickname());
            }

            for (int i = 0; i < numPlayers; i++) {
                int[] colors = {0,0,0,0,0};
                List<Integer> listTemp = new ArrayList<>();
                for (Student s : clouds[i].getStudents()) {
                    switch (s.getColor()){
                        case BLUE -> colors[0]++;
                        case YELLOW -> colors[1]++;
                        case RED -> colors[2]++;
                        case GREEN -> colors[3]++;
                        case PINK -> colors[4]++;
                    }
                }
                for (int j=0; j<5; j++) listTemp.add(colors[j]);
                studentsOnCloud.add(listTemp);
            }

            int myTowers = 0;
            if (myColor.equals(ColorTower.GREY)) myTowers = g;
            else if (myColor.equals(ColorTower.BLACK)) myTowers = b;
            else if (myColor.equals(ColorTower.WHITE)) myTowers = w;
            List<List<Student>> studentsInCanteen = new ArrayList<>();
            studentsInCanteen.add(getPlayer(owner).getDashboard().getCanteen().getStudents(Color.BLUE));
            studentsInCanteen.add(getPlayer(owner).getDashboard().getCanteen().getStudents(Color.YELLOW));
            studentsInCanteen.add(getPlayer(owner).getDashboard().getCanteen().getStudents(Color.RED));
            studentsInCanteen.add(getPlayer(owner).getDashboard().getCanteen().getStudents(Color.GREEN));
            studentsInCanteen.add(getPlayer(owner).getDashboard().getCanteen().getStudents(Color.PINK));

            List<String> enemiesNames = new ArrayList<>();
            List<ColorTower> enemiesColors = new ArrayList<>();
            List<Integer> enemiesTowers = new ArrayList<>();
            List<List<Integer>> enemiesEntrances = new ArrayList<>();
            List<List<Integer>> enemiesCanteen = new ArrayList<>();

            for(Player p : players){
                if(!p.equals(getPlayer(owner))){
                    enemiesNames.add(p.getNickname());
                    enemiesColors.add(p.getColor());
                    enemiesLastCards.add(p.getDashboard().getGraveyard());


                    if (p.getColor().equals(ColorTower.GREY)) enemiesTowers.add(g);
                    else if (p.getColor().equals(ColorTower.BLACK)) enemiesTowers.add(b);
                    else if (p.getColor().equals(ColorTower.WHITE)) enemiesTowers.add(w);

                    int[] colors = {0, 0, 0, 0, 0, 0};
                    for(Student s : p.getDashboard().getEntrance().getAllStudents()){
                        switch (s.getColor()){
                            case BLUE -> colors[0]++;
                            case YELLOW -> colors[1]++;
                            case RED -> colors[2]++;
                            case PINK -> colors[3]++;
                            case GREEN -> colors[4]++;
                        }
                    }
                    List<Integer> listTemp = new ArrayList<>();
                    for(int i=0; i<5; i++) listTemp.add(colors[i]);
                    enemiesEntrances.add(listTemp);

                    listTemp = new ArrayList<>();
                    listTemp.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.BLUE));
                    listTemp.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.YELLOW));
                    listTemp.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.RED));
                    listTemp.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.GREEN));
                    listTemp.add(p.getDashboard().getCanteen().getNumberStudentColor(Color.PINK));
                    enemiesCanteen.add(listTemp);
                }
            }

            return new GameStatus(currentPlayer, currentPhase, owner, myColor, myTowers, getPlayer(owner).getDashboard().getEntrance().getAllStudents(), studentsInCanteen, getPlayer(owner).getHand().getAllCards(), enemiesNames, enemiesColors, enemiesTowers, enemiesEntrances, enemiesCanteen, islandsId, owners, numTowers, studentsOnIsland, MN, professorsOwners, studentsOnCloud, getPlayer(owner).getDashboard().getGraveyard(), enemiesLastCards);
        }catch(Exception e){
            return new GameStatus(owner, "Error occourred while retrieving game's status...", currentPlayer);
        }
    }*/

    private void sendNotifyAll(){
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
        for(Player p : getAllPlayers()) notify(new GameReport(this, p));
    }

    public void sendErrorNote(String mistaker, String error, String currentPlayer){
        notify(new GameReport(mistaker, error, currentPlayer));
    }

    public boolean getCompleteRules(){
        return completeRules;
    }

    //--------------------------------------------------------------------------------------------
    //|                                             UTILS                                        |
    //--------------------------------------------------------------------------------------------

    private ArrayList<Student> extractFromBag(int numStudents){
        ArrayList<Student> students = new ArrayList<>();
        for(int i= 0; i<numStudents; i++){
            try {
                Student s = this.bag.getRandomStudent();
                students.add(s);
            } catch (NoMoreStudentsException e) {
                e.printStackTrace();
            }
        }

        return students;
    }
}