package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Dashboard {
    Card graveyard;
    private List<Tower> towers = new ArrayList<Tower>();
    private List<Student> students = new ArrayList<Student>();

    //students will be inizialized when the game start not when the players are created
    public Dashboard(int numPlayers, int posPlayer) {
        switch (numPlayers) {
            case 2:
                if (posPlayer == 0) {
                    for (int i = 0; i < 8; i++) {
                        towers.add(new Tower(i, ColorTower.BLACK));
                    }
                } else {
                    for (int i = 0; i < 8; i++) {
                        towers.add(new Tower(i, ColorTower.WHITE));
                    }
                }
            case 3:
                if (posPlayer == 0) {
                    for (int i = 0; i < 6; i++) {
                        towers.add(new Tower(i, ColorTower.BLACK));
                    }
                } else if (posPlayer == 1) {
                    for (int i = 0; i < 6; i++) {
                        towers.add(new Tower(i, ColorTower.WHITE));
                    }
                } else {
                    for (int i = 0; i < 6; i++) {
                        towers.add(new Tower(i, ColorTower.GREY));
                    }
                }
                //miss case:4
        }

    }
}
