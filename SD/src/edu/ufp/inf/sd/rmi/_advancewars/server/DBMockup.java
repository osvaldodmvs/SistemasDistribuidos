package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._04_diglib.server.Book;
import edu.ufp.inf.sd.rmi._04_diglib.server.User;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    private final ArrayList<edu.ufp.inf.sd.rmi._04_diglib.server.Book> books;// = new ArrayList();
    private final ArrayList<edu.ufp.inf.sd.rmi._04_diglib.server.User> users;// = new ArrayList();

    /**
     * This constructor creates and inits the database with some books and users.
     */
    public DBMockup() {
        books = new ArrayList<>();
        users = new ArrayList<>();
        //Add 3 books
        books.add(new edu.ufp.inf.sd.rmi._04_diglib.server.Book("Distributed Systems: principles and paradigms", "Tanenbaum"));
        books.add(new edu.ufp.inf.sd.rmi._04_diglib.server.Book("Distributed Systems: concepts and design", "Colouris"));
        books.add(new edu.ufp.inf.sd.rmi._04_diglib.server.Book("Distributed Computing Networks", "Tanenbaum"));
        //Add one user
        users.add(new edu.ufp.inf.sd.rmi._04_diglib.server.User("guest", "ufp"));
    }

    /**
     * Registers a new user.
     * 
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p) {
        if (!exists(u, p)) {
            users.add(new edu.ufp.inf.sd.rmi._04_diglib.server.User(u, p));
        }
    }

    /**
     * Checks the credentials of an user.
     * 
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    /**
     * Inserts a new book into the DigLib.
     * 
     * @param t title
     * @param a authors
     */
    public void insert(String t, String a) {
        books.add(new edu.ufp.inf.sd.rmi._04_diglib.server.Book(t, a));
    }

    /**
     * Looks up for books with given title and author keywords.
     * 
     * @param t title keyword
     * @param a author keyword
     * @return
     */
    public edu.ufp.inf.sd.rmi._04_diglib.server.Book[] select(String t, String a) {
        edu.ufp.inf.sd.rmi._04_diglib.server.Book[] abooks = null;
        ArrayList<edu.ufp.inf.sd.rmi._04_diglib.server.Book> vbooks = new ArrayList<>();
        // Find books that match
        for (int i = 0; i < books.size(); i++) {
            edu.ufp.inf.sd.rmi._04_diglib.server.Book book = (edu.ufp.inf.sd.rmi._04_diglib.server.Book) books.get(i);
            System.out.println("DB - select(): book[" + i + "] = " + book.getTitle() + ", " + book.getAuthor());
            if (book.getTitle().toLowerCase().contains(t.toLowerCase()) && book.getAuthor().toLowerCase().contains(a.toLowerCase())) {
                System.out.println("DB - select(): add book[" + i + "] = " + book.getTitle() + ", " + book.getAuthor());
                vbooks.add(book);
            }
        }
        // Copy Vector->Array
        abooks = new edu.ufp.inf.sd.rmi._04_diglib.server.Book[vbooks.size()];
        for (int i = 0; i < vbooks.size(); i++) {
            abooks[i] = (Book) vbooks.get(i);
        }
        return abooks;
    }
}
