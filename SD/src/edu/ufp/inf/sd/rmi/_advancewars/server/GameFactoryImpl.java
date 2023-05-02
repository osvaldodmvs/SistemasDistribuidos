package edu.ufp.inf.sd.rmi._advancewars.server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import static edu.ufp.inf.sd.rmi._advancewars.server.User.generateJWT;


public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryRI {

    private DBMockup db;
    private HashMap<String, GameSessionImpl> sessions;
    public GameFactoryImpl() throws RemoteException {
        super();
        this.db = new DBMockup();
        this.sessions = new HashMap<>();
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        if(db.exists(username)){
            return false;
        }
        //String jwt = generateJWT(username);
        db.register(username, pwd, "random jwt");
        return true;
    }

    @Override
    public GameSessionRI login(String username, String pwd) throws RemoteException {
        if(db.exists(username)) {
            if (db.validate(username, pwd)) {
                if (sessions.containsKey(username)) {
                    return sessions.get(username);
                } else {
                    GameSessionImpl session = new GameSessionImpl(this, username);
                    sessions.put(username, session);
                    return session;
                }
            }
        }
        return null;
    }

    public HashMap<String, GameSessionImpl> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, GameSessionImpl> sessions) {
        this.sessions = sessions;
    }

    public void deleteSession(String user){
        this.getSessions().remove(user);
    }

    public DBMockup getDb() {
        return db;
    }

    public void setDb(DBMockup db) {
        this.db = db;
    }
}
