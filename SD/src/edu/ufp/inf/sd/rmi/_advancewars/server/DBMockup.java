package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<User> users;// = new ArrayList();
    private final ArrayList<Game> games;// = new ArrayList();

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
        users = new ArrayList<>();
        games = new ArrayList<>();
    }

    /**
     * Registers a new user.
     * 
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p) {
        if (!exists(u)) {
            users.add(new User(u, p));
        }
    }

    /**
     * Checks if the username is taken
     * 
     * @param u username
     * @return
     */
    public boolean exists(String u) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    /**
     * Checks the credentials of an user.
     *
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean validate(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public User getUser(String username) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(username) == 0) {
                return usr;
            }
        }
        return null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Game> getGames() {
        return games;
    }


}
