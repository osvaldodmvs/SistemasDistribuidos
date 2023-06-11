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

    private final ArrayList<String> serverIds;

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
        games = new ArrayList<>();
        serverIds = new ArrayList<>();
    }

    public ArrayList<GameLobby> getGames() {
        return games;
    }

    public ArrayList<String> getServerIds() {
        return serverIds;
    }

    public void addGame(GameLobby game) {
        if(!games.contains(game))
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

    public boolean check_if_exists(String g) {
        for (GameLobby gg : this.getGames()) {
            if (gg.getId().compareTo(g) == 0) {
                return true;
            }
            return true;
        }
        return false;
    }

    public void addSvToList(String uid){
        if(!serverIds.contains(uid)){
            serverIds.add(uid);
            System.out.println("Added server to list");
        }
        else{
            System.out.println("Server already in list");
        }
    }

    public void printServers(){
        for(String s : serverIds){
            System.out.println(s);
        }
    }

}
