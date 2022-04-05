package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.EndGameException;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.NoMoreStudentsException;
import it.polimi.ingsw.model.exceptions.TooManyStudentsException;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


public class Translator {
    Game game;

    public Translator(boolean completeRules, int numPlayers){
        game = new Game(completeRules, numPlayers);
    }

    public void translateThis(Message message){

        switch(message.getAction()){
            case ADDME:
                //TODO Fix
                if(game.getRegisteredNumPlayers() == 1)
                    game.addPlayer(message.getSender(), ColorTower.WHITE);
                else if(game.getRegisteredNumPlayers() == 0 && game.getNumPlayers() == 3)
                    game.addPlayer(message.getSender(), ColorTower.GREY);
                else
                    game.addPlayer(message.getSender(), ColorTower.BLACK);
                break;



            case PLAYCARD:
                Card c;
                try {
                    c = game.getPlayer(message.getSender()).getHand().playCard(message.getPriority());
                    game.getPlayer(message.getSender()).getDashboard().setGraveyard(c);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;



            case MOVESTUDENT:
                Movable departure=null, arrival=null;
                switch(message.getDepartureType()){
                    case ENTRANCE:
                        try {
                            departure = game.getPlayer(message.getSender()).getDashboard().getEntrance();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case CANTEEN:
                        try{
                            departure = game.getPlayer(message.getSender()).getDashboard().getCanteen();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case ISLAND:
                        try {
                            departure = game.getIsland(message.getDepartureId());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        departure = null;
                }
                switch(message.getArrivalType()){
                    case ENTRANCE:
                        try {
                            arrival = game.getPlayer(message.getSender()).getDashboard().getEntrance();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case CANTEEN:
                        try{
                            arrival = game.getPlayer(message.getSender()).getDashboard().getCanteen();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case ISLAND:
                        try{
                            arrival = game.getIsland(message.getArrivalId());
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default:
                        arrival = null;
                }
                try {
                    game.moveStudent(message.getStudentId(), arrival, departure);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;



            case MOVEMOTHERNATURE:
                LinkedList<Island> islands = game.getAllIslands();
                int currentMNposition = islands.indexOf(game.getMotherNaturePosition());
                int numIslands = islands.size();
                int newMTposition = (currentMNposition + message.getMovement()) % numIslands;
                game.moveMotherNature(islands.get(newMTposition));
                break;



            case SELECTCLOUD:
                Cloud[] clouds = game.getAllClouds();
                try{
                    game.selectCloud(message.getSender(), clouds[message.getCloudPosition()]);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;



            case SHOWME:
                System.out.println("");
                System.out.println("");
                System.out.println("CURRENT BOARD:");
                islands = game.getAllIslands();
                for(Island i : islands){
                    String owner = "nobody";
                    if(i.getOwner() != null) owner = String.valueOf(i.getOwner());

                    System.out.print("Island "+i+", Owner "+owner+", Students: ");
                    ArrayList<Student> students = i.getAllStudents();
                    for(Student s : students) System.out.print(s+" ");
                    if(game.getMotherNaturePosition().equals(i)) System.out.print("MN");
                    System.out.print("\n");
                }
                Map<Color, Player> professors = game.getProfessors();
                System.out.print("PROFESSORS: ");
                for(Color co : Color.values()) System.out.print(co+": "+professors.get(co)+", ");
                ArrayList<Player> players = game.getAllPlayers();
                System.out.print("\nCLOUDS:\n");
                for(int i=0; i<game.getNumberOfClouds(); i++){
                    System.out.print("Cloud "+i+": ");
                    for(Color co : Color.values()) System.out.print(co+"="+game.getNumberOfStudentPerColorOnCloud(i,co)+", ");
                    System.out.print("\n");
                }
                for(Player p : players){
                    System.out.println("DASHBOARD of "+p+":");
                    System.out.print("Cards: ");
                    ArrayList<Card> cards = p.getHand().getAllCards();
                    for(Card ca : cards) System.out.print(ca+" ");
                    System.out.print("\nEntrance: ");
                    ArrayList<Student> students = p.getDashboard().getEntrance().getAllStudents();
                    for(Student s : students) System.out.print(s+" ");
                    System.out.print("\nCanteen: ");
                    for(Color col : Color.values()){
                        System.out.print(col+": "+p.getDashboard().getCanteen().getNumberStudentColor(col)+" ");
                    }
                    System.out.println("");
                }
                System.out.println("\n\n");
                break;



            default:
        }
    }

    public void endTurn() throws EndGameException{
        try {
            game.refillClouds();
        }catch(Exception e){
            if(e instanceof NoMoreStudentsException){
                throw new EndGameException("Game ended: the winner is "+getWinner());
            }
        }
    }

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
}