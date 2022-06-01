package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.Action;
import it.polimi.ingsw.controller.Location;
import it.polimi.ingsw.controller.Phase;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.rules.DefaultRule;
import it.polimi.ingsw.model.rules.InfluenceRule;
import it.polimi.ingsw.model.rules.Rule;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.view.GameReport;

import java.io.*;
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

    private final String JSON_PATH = "/characters.json";
    private String currentPlayer = null;
    private Phase currentPhase = Phase.PREGAME;
    private Map<Player, Card> playedCards;
    private int remainingMoves;
    private int remainingExchanges;
    private boolean usedCard;
    private boolean finishedGame = false;
    private boolean isLastTurn = false;
    private boolean nextLastTurn = false;
    private String winner=null;

    /**
     * Creator of the class Game. It represent the entire match and from Game it is possible to
     * reach every other entity and information. It only creates a Game, without any player in it
     * @param completeRules is a boolean value to determine if the match has to be played according
     *                      to complete or simple rules
     * @param numPlayers is the number of players who can join this match
     */
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

    /**
     * Method to be called when the match is finished
     */
    public void endGame(){
        finishedGame=true;
        winner=computeWinner();
        sendNotifyAll();
    }

    /**
     * This method initialize and refill all the clouds of the match, according to the number of
     * the players
     * @param numPlayers is the number of players who can join the match
     * @throws NoMoreStudentsException is thrown if, while this method is refilling a cloud, there
     */
    private void initClouds(int numPlayers) throws NoMoreStudentsException {
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

    /**
     * This method initialize and put a student on every island of the match
     * @param initBag is a temporary bag that contains only 10 students (2x every color) to fill
     *                the islands
     */
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

    /**
     * Temporary method needed only to test the software. It adds a (already built) player with
     * a pre-determined set of students in Entrance
     * @param p the Player to be added
     */
    //TODO TEMPORARY METHOD
    public void addPlayer(Player p){
        this.players.add(p);

        //AddPlayer has worked: update all the clients!
        //for(Player pl : getAllPlayers()) notify(getGameStatus(pl.getNickname()));
    }

    /**
     * Method to get the Map of the professors. Every professor is linked to the nickname of the player who owns
     * that professor
     * @return a Map<Color,Player>
     */
    public Map<Color, Player> getProfessors(){
        return this.professors;
    }

    /**
     * Method to add a Player to the match
     * @param nickname is the nickname of the player
     * @param color is the color of the player's towers
     */
    public void addPlayer(String nickname, ColorTower color){
        int numberOfStudents = this.numPlayers != 3 ? 7 : 9;
        ArrayList<Student> entranceStudents = extractFromBag(numberOfStudents);

        Player newPlayer = new Player(nickname, numPlayers, color, entranceStudents);

        players.add(newPlayer);

        if(players.size() < numPlayers){
            sendErrorNote(nickname, "Waiting for other players...", null);
        }
    }

    /**
     * Method to return the number of players in the match
     * @return an integer representing the number of the players
     */
    public int getNumPlayers(){
        return this.numPlayers;
    }

    /**
     * Method to return the number of the current players connected in the match
     * @return an integer representing the number of the players connected
     */
    public int getRegisteredNumPlayers(){
        return this.players.size();
    }

    /**
     * Method to return all the Islands
     * @return a LinkedList containing all the Islands of the match
     */
    public LinkedList<Island> getAllIslands(){
        return (LinkedList<Island>)islands.clone();
    }

    /**
     * Method to return the Island where MotherNature is positioned
     * @return the Island where MotherNature is positioned
     */
    public Island getMotherNaturePosition(){
        return motherNature.getPosition();
    }

    /**
     * This method returns the island with the right id
     * @param id is the identification number of the Island we want to get
     * @return the Island with that id
     * @throws NoIslandException if there is no such Island with this id
     */
    public Island getIsland(int id) throws NoIslandException {
        for(Island i : islands){
            //if(i.getId() == id) return i;
            if(i.checkContainedId(id)) return i;
        }
        throw new NoIslandException("There is no such island!");
    }

    /**
     * Method to return the entity Player with that Nickname
     * @param nickName is the identification String of the Player we want to get
     * @return the Player with that id
     * @throws NoPlayerException if there is no such a Player with that nickname
     */
    public Player getPlayer(String nickName) throws NoPlayerException{
        for(Player p : players){
            if(p.getNickname().equals(nickName)) return p;
        }
        throw new NoPlayerException("There is no such player!");
    }

    /**
     * Method to get all the Players connected to the match
     * @return an ArrayList containing all the Players
     */
    public ArrayList<Player> getAllPlayers(){
        return (ArrayList<Player>) players.clone();
    }

    /**
     * Method to play a card. The card identified by the priority will be discharged (moved to the player's
     * graveyard)
     * @param player is the Player who plays the card
     * @param priority identifies the card the player wants to play
     * @throws OverflowCardException
     * @throws AlreadyPlayedCardException
     */
    public void playCard(Player player, int priority) throws OverflowCardException, AlreadyPlayedCardException {
        Hand h = player.getHand();

        List<Card> cards = h.getAllCards();
        if(!playedCards.values().containsAll(cards)){
            Card card = new Card(priority, priority/2+priority%2);
            if(playedCards.containsValue(card)){
                throw new AlreadyPlayedCardException("Another player already played this card");
            }
        }

        Card c = h.playCard(priority);
        playedCards.put(player, c);
        player.getDashboard().setGraveyard(c);
        cards= h.getAllCards();
        if(cards.size()==0){
            this.isLastTurn=true;
        }
    }

    /**
     * This method is needed to reset the Hashtable playedCards. This Hashtable every turn remembers which cards
     * had been played by the players, to avoid that a player plays the same card of the previous players
     */
    public void resetPlayedCards(){
        playedCards = new Hashtable<>();;
    }

    /**
     * This method returns the max distance MotherNature can travel
     * @param p is the player who has to move MotherNature
     * @return the max distance MotherNature can travel
     */
    public int getMaximumMNMovement(Player p){
        return playedCards.get(p).getMovement();
    }

    /**
     * Method to move MotherNature
     * @param island is the Island where we want to move MotherNature
     * @param enableMovement When parameter enableMovement is true the method has the regular behaviour.
     *     If enableMovement is false, it will be checked if there's an active card.
     * @throws NoActiveCardException
     * @throws NoMoreTokensException
     */
    public void moveMotherNature(Island island, boolean enableMovement) throws NoActiveCardException, NoMoreTokensException {
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
                    //System.out.println("il giocatore "+p.getNickname()+"ha nella dashbord num torri"+ p.getDashboard().getTowers());
                    if (p.getColor() == report.getOwner()) {
                        p.getDashboard().addTowers(report.getTowerNumbers());
                    }
                    if (p.getColor() == higherInfluence) {
                        if(report.getOwner()==null){
                            p.getDashboard().removeTowers(1);
                        }
                        else{
                            p.getDashboard().removeTowers(report.getTowerNumbers());
                        }
                        if(p.getDashboard().getTowers()==0){
                            this.winner=p.getNickname(); // i could use also compute winner but in this case i already know who won
                            this.finishedGame=true;
                            sendNotifyAll();
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

    /**
     * Method to merge two islands. This method deletes the two islands and replaces them with a single island
     * containing all the students and towers
     * @param i1 is the first Island
     * @param i2 is the second Island
     * @return the new Island resulted from the merged
     */
    public Island mergeIsland(Island i1, Island i2){
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
            this.isLastTurn=true;
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

    /**
     * Method to move a Student from a Location to another one
     * @param studentId is the id of the student we want to move
     * @param arrival is the object (must be a Location) where we want to move the Student
     * @param departure is the object (must be a Location) where the Student is
     * @throws NoSuchStudentException if there is no Student with such id on departure
     * @throws CannotAddStudentException if it is not possible to move the Student between these Locations
     */public void moveStudent(int studentId, Movable arrival, Movable departure) throws NoSuchStudentException, CannotAddStudentException {
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

    /**
     * Method to swap two Students between two Locations
     * @param studentId1 is the id of the first Student to be swapped
     * @param studentId2 is the id of the second Student to be swapped
     * @param arrival is the object (must be a Location) where the first Student is placed (and where we want to
     *                move the second Student)
     * @param departure is the object (must be a Location) where the second Student is placed (and where we want
     *                  to move the first Student)
     * @throws NoSuchStudentException if one of the two Students is missing
     * @throws CannotAddStudentException is it is not possible to swap the two Students
     */
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

    }

    /**
     * Method to be called at the end of a complete turn to refill all the empty clouds
     * @throws NoMoreStudentsException if the Bag is empty and it is no more possible to extract Students
     */
    public void refillClouds() throws NoMoreStudentsException {
        for(int i=0; i<clouds.length; i++){
            clouds[i].setIsFull(true);
        }
        for (int i=0; i<clouds.length; i++){
            if (numPlayers==3) clouds[i].refillCloud(bag.getRandomStudent(4));
            else clouds[i].refillCloud(bag.getRandomStudent(3));
        }
    }

    /**
     * Method to let a Player to select a Cloud
     * @param playerNick is the Player who wants to select this Cloud
     * @param cloud is the Cloud the Player has selected
     * @throws NoPlayerException if there is no such Player with this id
     */
    public void selectCloud(String playerNick, Cloud cloud) throws NoPlayerException{
        ArrayList<Student> students = cloud.chooseCloud();
        for (Student s : students) getPlayer(playerNick).getDashboard().getEntrance().addStudent(s);

        //This will always be the last action of each player per round, so the rules can be reset now
        this.activeCard = -1;
        this.currentRule = new DefaultRule();

        //SelectCloud has worked: update all the clients!
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
    }

    /**
     * Method to return all the Clouds available in the game
     * @return an array (length=4) containing from 2 to 4 clouds
     */
    public Cloud[] getAllClouds(){
        return clouds;
    }

    /**
     * Method to set who is the current Player
     * @param nickname is the current Player
     * @param sendNotify
     */
    public void setCurrentPlayer(String nickname, boolean sendNotify){
        currentPlayer = nickname;
        if(sendNotify)
            sendNotifyAll();
    }

    /**
     * Method to set the current Phase
     * @param phase is the phase we want to set as current
     */
    public void setCurrentPhase(Phase phase){
        currentPhase = phase;
        sendNotifyAll();
    }

    /**
     * Method to return the nickname of the current Player
     * @return the String representing the current Player's id
     */
    public String getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Method to return the nickname of the current Phase
     * @return the current Phase of the match
     */
    public Phase getCurrentPhase(){
        return currentPhase;
    }

    /**
     * Method to get a flag signaling if the game is ended or still running
     * @return a boolean representing this flag
     */
    public Boolean getFinishedGame(){ return finishedGame;}

    /**
     * Method to get a flag representing if the current turn is the last one or not
     * @return a boolean representing this flag
     */
    public Boolean getIsLastTurn(){ return isLastTurn; }

    /**
     * Method to get the nickname of the winner
     * @return the nickname of the winner of the match
     */
    public String getWinner(){ return winner;}

    /**
     * The variable remainingMoves is needed to remember how many times the current player can still move a
     * student. This method reset the variable to the maximum number of moveStudents, according to the number
     * of players
     */
    public void resetRemainingMoves(){
        this.remainingMoves = this.numPlayers==3 ? 4 : 3;
    }

    /**
     * This method reduces by one the variable remainingMoves if this is >0
     * @param activeCard indicates if there is a card activated. This is needed to avoid that, when moving a
     *                   student because of a special power, remainingMoves is decreased
     */
    public void reduceRemainingMoves(boolean activeCard){
        if(!activeCard)
            this.remainingMoves--;
        if(remainingMoves != 0 && !activeCard)
            sendNotifyAll();
    }

    /**
     * This method returns the numer of moveStudent the current player can still perform
     * @return
     */
    public int getRemainingMoves(){
        return remainingMoves;
    }

    //-------------------------------------------------------------------------------------
    //|                                     CHARACTERS                                    |
    //-------------------------------------------------------------------------------------

    /**
     * Method to initialize 3 random characters from the JSON file if the rules of the match are complete
     * @return an Array containing the 3 characters
     * @throws IOException if something goes wrong and the JSON file cannot be read
     */
    public Character[] initCardsFromJSON() throws IOException{
        try (InputStream inputStream = getClass().getResourceAsStream(JSON_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            JSONCharacter[] jsonCharacters = gson.fromJson(reader, JSONCharacter[].class);
            List<Character> allCharacters = new ArrayList<>();
            for (JSONCharacter jc : jsonCharacters) {
                switch (jc.getTypeCharacter()) {
                    case MOVEMENT:
                        ArrayList<Student> s = extractFromBag(jc.getParams().getNumThingOnIt());
                        allCharacters.add(new MovementCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), s, jc.getParams()));
                        break;

                    case INFLUENCE:
                        allCharacters.add(new InfluenceCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams()));
                        break;

                    case PROFESSOR:
                        allCharacters.add(new ProfessorCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost()));
                        break;

                    case MOTHERNATURE:
                        allCharacters.add(new MotherNatureCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams()));
                        break;

                    case ACTION:
                        allCharacters.add(new ActionCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), jc.getParams()));
                        break;

                    case EXCHANGE:
                        ArrayList<Student> s2 = extractFromBag(jc.getParams().getNumThingOnIt());
                        allCharacters.add(new ExchangeCharacter(jc.getId(), jc.getTypeCharacter(), jc.getDesc(), jc.getDesc_short(), jc.getCost(), s2, jc.getParams()));
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

    /**
     * This method returns the number of the active card (-1 if no card is active)
     * @return the number of the activated card (0, 1 or 2) or -1 if no card is active
     */
    public int getActiveCard(){
        return this.activeCard;
    }

    /**
     * This method returns a copy of all the three characters available.
     * @return an Array containing the three characters available
     */
    public Character[] getCharactersCards(){
        return this.charactersCards.clone();
    }

    /**
     * This method returns the current Rule
     * @return the current Rule
     */
    //TODO Debug Method
    public Rule getCurrentRule(){
        return this.currentRule;
    }

    /**
     * Method needed only for test. This method sets the character
     * @param characters is the character to be introduced in the match
     */
    //TODO Debug Method
    public void setCharactersCards(Character[] characters){
        this.charactersCards = characters.clone();
    }

    /**
     * This method returns a boolean variable indicating if the activated card has been used or not
     * @return this boolean variable
     */
    public boolean getUsedCard(){
        return this.usedCard;
    }

    /**
     * This method sets the value of the variable usedCard
     * @param usedCard is the boolean value to be assigned to the variable usedCard
     */
    public void setUsedCard(boolean usedCard){
        this.usedCard = usedCard;
    }

    /**
     * This method is called when a character is activated
     * @param activePlayer is the Player who has activated the card
     * @param card is the card activated by the Player
     * @return a boolean value. If it is true, it means that the player has to perform other moves, if false,
     * there is no need of other specific moves
     * @throws NoCharacterSelectedException is thrown if the card the player wants to activated is not 0, 1 or 2
     * @throws NotEnoughMoneyException is thrown if the player hasn't enough money to active the card
     * @throws IllegalMoveException is thrown it is not possible to use a card in this phase
     */
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

    /**
     * It's a method which returns true if in this phase it is possible to exchange two students, false
     * otherwise
     * @return the boolean result
     */
    public boolean canExchange(){
        return (this.currentRule.getMaximumExchangeMoves() != 0);
    }

    /**
     * Some characters allow to exchange students more than once in a single turn. Therefore a counter is
     * needed and also a method to reset the counter at the end of the turn
     */
    public void resetRemainingExchanges(){
        this.remainingExchanges = this.currentRule.getMaximumExchangeMoves();
        sendNotifyAll();
    }

    /**
     * This method reduces of one the variable remainingeExchange needed to count how many times a player is
     * still allowed to exchange Players
     */
    public void reduceRemainingExchanges(){
        this.remainingExchanges--;

        if(remainingExchanges == 0){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        }

        sendNotifyAll();
    }

    /**
     * Method to return the number of exchangeStudents still allowed
     * @return an integer representing the number of exchangeStudents still allowed
     */
    public int getRemainingExchanges(){
        return remainingExchanges;
    }

    /**
     * This method returns the requested actions
     * @return the requested Action
     * @throws NoActiveCardException is thrown if there is no active card
     */
    public Action getRequestedAction() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        ActionCharacter c1 = (ActionCharacter) this.charactersCards[activeCard];

        return c1.getType();
    }

    /**
     * This method returns the allowed Locations from where a Student can be moved, according to the phase of
     * the turn and the active card
     * @return a Set containing all the type of allowed Locations
     * @throws NoActiveCardException if no card has been activated
     */
    public Set<Location> getAllowedDepartures() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter c1 = (MovementCharacter) this.charactersCards[activeCard];

        return c1.getAllowedDepartures();
    }

    /**
     * This method returns the allowed Locations to where a Student can be moved, according to the phase of
     * the turn and the active card
     * @return a Set containing all the type of allowed Locations
     * @throws NoActiveCardException
     */
    public Set<Location> getAllowedArrivals() throws NoActiveCardException{
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter c1 = (MovementCharacter) this.charactersCards[activeCard];

        return c1.getAllowedArrivals();
    }

    /**
     * This method disable an island (if a token is placed)
     * @param i indicates the island to be disabled
     * @throws NoActiveCardException if there is no active card
     * @throws NoMoreTokensException if all 4 block tokens available are already placed
     */
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

    /**
     * This method disable a Color after a character is activate
     * @param player is the Player who has activated the special power
     * @param c is the Color to be blocked
     * @throws NoActiveCardException if no active card is activated or the card activated doesn't allow to
     * block a color
     */
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

    /**
     * This method is to be activated only after the right character has been activated. This method puts back
     * in the Bag three students of that Color of every Player
     * @param color is the Color of the Students to be put back again in the Bag
     * @throws NoActiveCardException if there is no active card or the active card doesn't allow to put back
     * three Students in the Bag
     * @throws NoSuchStudentException cannot be thrown
     */
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

    /**
     * This method returns the bonus distance MotherNature can be moved
     * @return an integer representing this bonus distance
     */
    public int getMotherNatureExtraMovement(){
        return this.currentRule.getMotherNatureExtraMovement();
    }

    /**
     * This method returns true if all the clouds are empty (because they have already been chosen by the
     * players, false otherwise
     * @return the boolean value if the clouds need to be refilled
     */
    public boolean needsRefill(){
        if(this.activeCard == -1) return false;

        MovementCharacter mc = null;
        try{
            mc = (MovementCharacter) this.charactersCards[activeCard];
        } catch(ClassCastException ex){
            return false;
        }

        return mc.isRefill();
    }

    /**
     * Method to refill the characters'cards that contain Students
     * @throws NoActiveCardException if there is no active Card in this turn
     * @throws NoMoreStudentsException if there are no more Students in the Bag
     * @throws CannotAddStudentException if the active Card doesn't contain any Student
     */
    public void refillActiveCard() throws NoActiveCardException, NoMoreStudentsException, CannotAddStudentException {
        if(this.activeCard == -1) throw new NoActiveCardException("No Active Card");

        MovementCharacter mc = (MovementCharacter) this.charactersCards[activeCard];
        mc.addStudent(bag.getRandomStudent());

        //if(this.activeCard != -1){
            this.activeCard = -1;
            this.currentRule = new DefaultRule();
        //}
    }

    //--------------------------------------------------------------------------------------------
    //|                                             NOTIFIES                                     |
    //--------------------------------------------------------------------------------------------

    private void sendNotifyAll(){
        //for(Player p : getAllPlayers()) notify(getGameStatus(p.getNickname()));
        for(Player p : getAllPlayers()) notify(new GameReport(this, p));
    }

    /**
     * Method to notify a mistake (if the action requested by the player is illegal) to the RemoteView
     * @param mistaker is the nickname of the Player who sent the illegal move
     * @param error is the message of error to be transmitted to the Player
     * @param currentPlayer is the Player who is supposed to play in this turn
     */
    public void sendErrorNote(String mistaker, String error, String currentPlayer){
        notify(new GameReport(mistaker, error, currentPlayer, false));
    }

    /**
     * Method to notify all the Players if one Player has left the match
     * @param nickname is the Player who disconnetted from the match
     */
    public void sendDisconnectionAll(String nickname){
        this.finishedGame = true;
        String errorMessage = nickname + " disconnected!\nThe game is finished\n";
        for(Player p : getAllPlayers())
            notify(new GameReport(p.getNickname(), errorMessage, p.getNickname(), true));
    }

    /**
     * This method returns true if the match is ruled by the complete rules, false otherwise
     * @return true if the match is ruled by the complete rules, false otherwise
     */
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
                setLastTurn(true);
                e.printStackTrace();
            }
        }

        return students;
    }

    private String computeWinner() {
        ColorTower max;
        int black = 0;
        int white = 0;
        int grey = 0;
        LinkedList<Island> islands = this.getAllIslands();
        for (Island i : islands) {
            if (i.getOwner() != null) {
                switch (i.getOwner()) {
                    case BLACK -> black += i.getReport().getTowerNumbers();
                    case WHITE -> white += i.getReport().getTowerNumbers();
                    case GREY -> grey += i.getReport().getTowerNumbers();
                }
            }
        }
        if (black >= white && black >= grey) max= ColorTower.BLACK;
        else if (white >= black && white >= grey) max= ColorTower.WHITE;
        else max= ColorTower.GREY;
        for (Player p : players){
            if(p.getColor().equals(max)) return p.getNickname();
        }
        return null;
    }

    /**
     * Method to set the variable lastTurn if this is the last turn
     * @param lastTurn is the value we want to apply to lastTurn
     */
    public void setLastTurn(Boolean lastTurn){
        isLastTurn=lastTurn;
    }

    /**
     * Method to set the variable nextLastTurn if the next turn is the last one
     * @param lastTurn is the value we want to apply to nextLastTurn
     */
    public void setNextLastTurn(Boolean lastTurn){
        nextLastTurn=lastTurn;
    }
}