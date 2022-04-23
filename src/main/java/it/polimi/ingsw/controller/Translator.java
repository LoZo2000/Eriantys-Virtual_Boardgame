package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.Character;
import it.polimi.ingsw.model.characters.CharacterType;
import it.polimi.ingsw.model.characters.MovementCharacter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;


public class Translator {
    private Game game;
    private final boolean completeRules;

    public Translator(boolean completeRules, int numPlayers){
        this.completeRules = completeRules;
        game = new Game(completeRules, numPlayers);
    }

    public boolean translateThis(Message message) throws IllegalMoveException, NoCharacterSelectedException, NoActiveCardException, NotEnoughMoneyException, NoMoreTokensException {

        switch(message.getAction()){

            /*case CREATEMATCH:
                if(game.getRegisteredNumPlayers() == 1)
                    game.addPlayer(message.getSender(), ColorTower.WHITE);
                else if(game.getRegisteredNumPlayers() == 0 && game.getNumPlayers() == 3)
                    game.addPlayer(message.getSender(), ColorTower.GREY);
                else
                    game.addPlayer(message.getSender(), ColorTower.BLACK);
                return false;*/

            case ADDME:         //To add a player in the current match
                //TODO Fix
                if(game.getRegisteredNumPlayers() == 1)
                    game.addPlayer(message.getSender(), ColorTower.WHITE);
                else if(game.getRegisteredNumPlayers() == 0 && game.getNumPlayers() == 3)
                    game.addPlayer(message.getSender(), ColorTower.GREY);
                else
                    game.addPlayer(message.getSender(), ColorTower.BLACK);
                return false;



            case PLAYCARD:      //To play the assistant card
                Card c;
                try {
                    c = game.getPlayer(message.getSender()).getHand().playCard(message.getPriority());
                    game.getPlayer(message.getSender()).getDashboard().setGraveyard(c);
                }
                catch(Exception e){
                    throw new IllegalMoveException("There is no such a card");
                }
                return false;

            case MOVESTUDENT:   //To move a student (from ENTRANCE to ISLAND or CANTEEN) (or from CARD_ISLAND or CARD_CANTEEN if active card)
                Movable departure=null, arrival=null;
                switch(message.getDepartureType()){
                    case ENTRANCE:
                        try {
                            departure = game.getPlayer(message.getSender()).getDashboard().getEntrance();
                        }catch(Exception e){
                            //Cannot fail (impossible to enter this branch)!!!
                            e.printStackTrace();
                        }
                        break;
                    case CANTEEN:
                        try{
                            departure = game.getPlayer(message.getSender()).getDashboard().getCanteen();
                        }catch(Exception e){
                            //Cannot fail (impossible to enter this branch)!!!
                            e.printStackTrace();
                        }
                        break;
                    case ISLAND:
                        try {
                            departure = game.getIsland(message.getDepartureId());
                        }catch(Exception e){
                            throw new IllegalMoveException("This island doesn't exist!");
                        }
                        break;
                    case CARD_ISLAND:
                        departure = getMovementCharacterCard(Location.CARD_ISLAND);
                        break;
                    case CARD_CANTEEN:
                        departure = getMovementCharacterCard(Location.CARD_CANTEEN);
                        break;
                    default:
                        departure = null;
                }
                switch(message.getArrivalType()){
                    case ENTRANCE:
                        try {
                            arrival = game.getPlayer(message.getSender()).getDashboard().getEntrance();
                        }catch (Exception e){
                            //Cannot fail (impossible to enter this branch)!!!
                            e.printStackTrace();
                        }
                        break;
                    case CANTEEN:
                        try{
                            arrival = game.getPlayer(message.getSender()).getDashboard().getCanteen();
                        }catch(Exception e){
                            //Cannot fail (impossible to enter this branch)!!!
                            e.printStackTrace();
                        }
                        break;
                    case ISLAND:
                        try{
                            arrival = game.getIsland(message.getArrivalId());
                        }catch(Exception e){
                            throw new IllegalMoveException("This island doesn't exist!");
                        }
                        break;
                    case CARD_ISLAND:
                        arrival = getMovementCharacterCard(Location.CARD_ISLAND);
                        break;
                    case CARD_CANTEEN:
                        arrival = getMovementCharacterCard(Location.CARD_CANTEEN);
                        break;
                    default:
                        arrival = null;
                }
                try {
                    game.moveStudent(message.getStudentId(), arrival, departure);
                }catch (Exception e){
                    throw new IllegalMoveException("Student, arrival or departure missing...");
                }

                try {
                    if(this.game.needsRefill())
                        this.game.refillActiveCard();
                }catch(NoActiveCardException | NoMoreStudentsException e){
                    e.printStackTrace();
                }
                return false;



            case MOVEMOTHERNATURE:  //To move MotherNature
                LinkedList<Island> islands = game.getAllIslands();
                int currentMNposition = islands.indexOf(game.getMotherNaturePosition());
                int numIslands = islands.size();
                int newMTposition = (currentMNposition + message.getMovement()) % numIslands;
                game.moveMotherNature(islands.get(newMTposition), true);
                return false;



            case SELECTCLOUD:       //To select a cloud
                Cloud[] clouds = game.getAllClouds();
                //TODO Add a check if the position of the cloud is ok
                try{
                    game.selectCloud(message.getSender(), clouds[message.getCloudPosition()]);
                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;

            case SHOWME:
                showMe();
                return false;

            case USEPOWER:
                USEPOWERmessage um = (USEPOWERmessage) message;
                Player p = null;
                try {
                    p = this.game.getPlayer(um.getSender());
                }catch (NoPlayerException ex){
                    ex.printStackTrace();
                }
                return this.game.usePower(p, um.getCharacterCard());

            case EXCHANGESTUDENT:
                //Movable departure, arrival;
                EXCHANGESTUDENTmessage exchangeMessage = (EXCHANGESTUDENTmessage) message;
                p = null;
                try {
                    p = this.game.getPlayer(exchangeMessage.getSender());
                }catch (NoPlayerException ex){
                    ex.printStackTrace();
                    return false;
                }
                switch(exchangeMessage.getDepartureType()){
                    case ENTRANCE:
                        departure = p.getDashboard().getEntrance();
                        break;
                    case CANTEEN:
                        departure = p.getDashboard().getCanteen();
                        break;
                    case CARD_EXCHANGE:
                        departure = getMovementCharacterCard(Location.CARD_EXCHANGE);
                        break;
                    default:
                        departure = null;
                }
                switch(exchangeMessage.getArrivalType()){
                    case ENTRANCE:
                        arrival = p.getDashboard().getEntrance();
                        break;
                    case CANTEEN:
                        arrival = p.getDashboard().getCanteen();
                        break;
                    case CARD_EXCHANGE:
                        arrival = getMovementCharacterCard(Location.CARD_EXCHANGE);
                        break;
                    default:
                        arrival = null;
                }
                try {
                    game.exchangeStudent(exchangeMessage.getStudentId(), exchangeMessage.getStudentId2(), arrival, departure);
                } catch (Exception e) {
                    throw new IllegalMoveException("Student, arrival or departure missing...");
                }
                return false;

            case BLOCK_ISLAND:
                blockIsland(message);
                return false;

            case ISLAND_INFLUENCE:
                calculateIslandInfluence(message);
                return false;

            case BLOCK_COLOR:
                blockColor((ChooseColorMessage) message);
                return false;

            case PUT_BACK:
                putBackStudents((ChooseColorMessage) message);
                return false;

            default:
                return false;
        }
    }

    public Action getRequestedAction() throws NoActiveCardException{
        return this.game.getRequestedAction();
    }

    private void blockIsland(Message message) throws NoActiveCardException, NoMoreTokensException, IllegalMoveException {
        ChooseIslandMessage cm = (ChooseIslandMessage) message;
        Island i;
        try {
            i = this.game.getIsland(cm.getIdIsland());
        }catch(NoIslandException e){
            throw new IllegalMoveException("This island doesn't exist!");
        }

        this.game.disableIsland(i);
    }

    private void calculateIslandInfluence(Message message) throws NoActiveCardException, IllegalMoveException {
        ChooseIslandMessage cm = (ChooseIslandMessage) message;
        Island i;
        try {
            i = this.game.getIsland(cm.getIdIsland());
        }catch(NoIslandException e){
            throw new IllegalMoveException("This island doesn't exist!");
        }

        this.game.moveMotherNature(i, false);
    }

    private void blockColor(ChooseColorMessage message) throws NoActiveCardException {
        Player p = null;
        try {
            p = this.game.getPlayer(message.getSender());
        } catch (NoPlayerException e) {
            e.printStackTrace();
        }
        this.game.disableColor(p, message.getChosenColor());
    }

    private void putBackStudents(ChooseColorMessage message) throws NoActiveCardException {
        this.game.putBackInBag(message.getChosenColor());
    }

    private void showMe() {
        System.out.println("");
        System.out.println("");
        System.out.println("CURRENT BOARD:");
        LinkedList<Island> islands = game.getAllIslands();
        for (Island i : islands) {
            String owner = "nobody";
            if (i.getOwner() != null) owner = String.valueOf(i.getOwner());

            System.out.print("Island " + i + ", Owner " + owner + ", Students: ");
            ArrayList<Student> students = i.getAllStudents();
            for (Student s : students) System.out.print(s + " ");
            if (game.getMotherNaturePosition().equals(i)) System.out.print("MN");
            if(i.getProhibition()) System.out.print("\u001B[31mBLOCK\u001B[0m");
            System.out.print("\n");
        }
        Map<Color, Player> professors = game.getProfessors();
        System.out.print("PROFESSORS: ");
        for (Color co : Color.values()) System.out.print(co + ": " + professors.get(co) + ", ");
        ArrayList<Player> players = game.getAllPlayers();
        System.out.print("\nCLOUDS:\n");
        for (int i = 0; i < game.getNumberOfClouds(); i++) {
            System.out.print("Cloud " + i + ": ");
            for (Color co : Color.values())
                System.out.print(co + "=" + game.getNumberOfStudentPerColorOnCloud(i, co) + ", ");
            System.out.print("\n");
        }
        for (Player p : players) {
            System.out.println("\nDASHBOARD of " + p + ":");
            System.out.print("CARDS: ");
            ArrayList<Card> cards = p.getHand().getAllCards();
            for (Card ca : cards) System.out.print(ca + " ");
            System.out.print("\nEntrance: ");
            ArrayList<Student> students = p.getDashboard().getEntrance().getAllStudents();
            for (Student s : students) System.out.print(s + " ");
            System.out.println("\n\u001B[1m\u001B[34mCanteen: \u001B[0m");
            for (Color col : Color.values()) {
                System.out.println("\t\u001B[1m"+col + "\u001B[0m: " + p.getDashboard().getCanteen().getStudents(col) + " (Num: " + p.getDashboard().getCanteen().getNumberStudentColor(col) +")");
            }
            if(completeRules)
                System.out.print("\nCoins: " + p.getCoins());
            System.out.println("");
        }

        if (completeRules){
            Character[] characters = this.game.getCharactersCards();
            System.out.println("\nCARDS: \n");
            for (Character c : characters) {
                System.out.println(c.toString());
            }


            System.out.println();
            if (this.game.getActiveCard() == -1) {
                System.out.println("\u001B[31mNo active card\u001B[0m");
            } else {
                System.out.println("\u001B[34mThe active card is " + this.game.getActiveCard() + "\u001B[0m");
                try {
                    System.out.println("\u001B[34mThe nextAction is " + this.game.getRequestedAction() + "\u001B[0m");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            //TODO DEBUG
            System.out.println();
            System.out.println(game.getCurrentRule().toString());

            System.out.println("\n\n");
        }
    }

    private MovementCharacter getMovementCharacterCard(Location typeCard){
        Character[] characters = this.game.getCharactersCards();
        for(Character ch : characters){
            if(ch.getTypeCharacter() == CharacterType.MOVEMENT){
                MovementCharacter mc = (MovementCharacter) ch;
                if(mc.getLocationType() == typeCard)
                    return mc;
            }
        }
        return null;
    }


    public Set<Location> getAllowedDepartures() throws NoActiveCardException{
        return this.game.getAllowedDepartures();
    }

    public Set<Location> getAllowedArrivals() throws NoActiveCardException{
        return this.game.getAllowedArrivals();
    }

    //Function called when the game ends
    //By now, the only ending condition implemented is:
    //                                      -when there are no more students in the bag
    public void endTurn() throws EndGameException{
        try {
            game.refillClouds();
        }catch(Exception e){
            if(e instanceof NoMoreStudentsException){
                throw new EndGameException("Game ended: the winner is "+getWinner());
            }
        }
    }

    //Check the winner counting the number of towers in the game:
    private ColorTower getWinner(){
        int black=0;
        int white=0;
        int grey=0;
        LinkedList<Island> islands = game.getAllIslands();
        for(Island i : islands){
            if(i.getOwner()!=null){
                switch (i.getOwner()){
                    case BLACK -> black += i.getReport().getTowerNumbers();
                    case WHITE -> white += i.getReport().getTowerNumbers();
                    case GREY -> grey += i.getReport().getTowerNumbers();
                }
            }
        }
        if(black>=white && black>=grey) return ColorTower.BLACK;
        if(white>=black && white>=grey) return ColorTower.WHITE;
        return ColorTower.GREY;
    }

    public Game getGame(){
        return game;
    }
}
