package edu.ufp.inf.sd.rabbitmqservices._advancewars.server;

import edu.ufp.inf.sd.rabbitmqservices._advancewars.server.GameLobby;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<GameLobby> games;// = new ArrayList();

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
        games = new ArrayList<>();
    }

    public ArrayList<GameLobby> getGames() {
        return games;
    }

    public void addGame(GameLobby game) {
        games.add(game);
    }

    public GameLobby getGame(String g) {
        for (GameLobby game : this.games) {
            if (game.getId().compareTo(g) == 0) {
                return game;
            }
        }
        return null;
    }
}
