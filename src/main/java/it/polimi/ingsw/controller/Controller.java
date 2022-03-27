package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.LinkedList;
import java.util.Random;


public class Controller {
    Game game;

    public Controller(boolean completeRules, int numPlayers){
        game = new Game(completeRules, numPlayers);
    }

    public void doSomeShit(String playerNick, Action action, String object){

        switch(action){



            case ADDME:             //Object format: <playerId>
                if(game.getRegisteredNumPlayers() == 1)
                    game.addPlayer(playerNick, ColorTower.WHITE);
                else if(game.getRegisteredNumPlayers() == 0 && game.getNumPlayers() == 3)
                    game.addPlayer(playerNick, ColorTower.GREY);
                else
                    game.addPlayer(playerNick, ColorTower.BLACK);
                break;



            case PLAYCARD:          //Object format: <priorityOfCard>
                Card c;
                try {
                    c = game.getPlayer(object).getHand().playCard(Integer.parseInt(object));
                    game.getPlayer(object).getDashboard().setGraveyard(c);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                break;



            case MOVESTUDENT:       //Object format: <studentId.departureType.departureId.arrivalType.arrivalId>
                Movable departure, arrival;
                String[] values = object.split(".");
                int studentId = Integer.parseInt(values[0]);
                int departureType = Integer.parseInt(values[0]);
                int departureId = Integer.parseInt(values[0]);
                int arrivalType = Integer.parseInt(values[0]);
                int arrivalId = Integer.parseInt(values[0]);
                switch(departureType){
                    case 0: //From the player's entrance
                        departure = game.getPlayer(playerNick).getDashboard().getEntrance();
                        break;
                    case 1: //From the player's canteen
                        departure = game.getPlayer(playerNick).getDashboard().getCanteen();
                        break;
                    case 2: //From an island
                        departure = game.getIsland(departureId);
                        break;
                    default:
                        departure = null;
                }
                switch(arrivalType){
                    case 0: //To the player's entrance
                        arrival = game.getPlayer(playerNick).getDashboard().getEntrance();
                        break;
                    case 1: //To the player's canteen
                        arrival = game.getPlayer(playerNick).getDashboard().getCanteen();
                        break;
                    case 2: //To an island
                        arrival = game.getIsland(arrivalId);
                        break;
                    default:
                        arrival = null;
                }
                game.moveStudent(studentId, arrival, departure);
                break;



            case MOVEMOTHERNATURE:  //Object format: <movement>
                int movement = Integer.parseInt(object);
                LinkedList<Island> islands = game.getAllIslands();
                int currentMNposition = islands.indexOf(game.getMotherNaturePosition());
                int numIslands = islands.size();
                int newMTposition = (currentMNposition + movement) % numIslands;
                game.moveMotherNature(islands.get(newMTposition));
                break;



            case SELECTCLOUD:       //Object format: <cloudPositiob>
                int index = Integer.parseInt(object);
                Cloud[] clouds = game.getAllClouds();
                game.selectCloud(playerNick, clouds[index]);
                break;



            default:
        }
    }
}