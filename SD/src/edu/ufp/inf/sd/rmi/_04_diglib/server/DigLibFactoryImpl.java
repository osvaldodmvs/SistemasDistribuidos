package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class DigLibFactoryImpl extends UnicastRemoteObject implements DigLibFactoryRI {

    private DBMockup db;
    private HashMap<String,DigLibSessionImpl> sessions;
    public DigLibFactoryImpl() throws RemoteException {
        super();
        this.db = new DBMockup();
        this.sessions = new HashMap<>();
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        if(db.exists(username, pwd)){
            return false;
        }
        db.register(username, pwd);
        return true;
    }

    @Override
    public DigLibSessionRI login(String username, String pwd) throws RemoteException {
        if(db.exists(username, pwd)){
            if(sessions.containsKey(username)){
                return sessions.get(username);
            }
            else{
                DigLibSessionImpl session = new DigLibSessionImpl(this,username);
                sessions.put(username, session);
                return session;
            }
        }
        return null;
    }

    public DBMockup getDb() {
        return db;
    }

    public void setDb(DBMockup db) {
        this.db = db;
    }

    public HashMap<String, DigLibSessionImpl> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String, DigLibSessionImpl> sessions) {
        this.sessions = sessions;
    }

    public void deleteSession(String user){
        this.getSessions().remove(user);
    }

}
