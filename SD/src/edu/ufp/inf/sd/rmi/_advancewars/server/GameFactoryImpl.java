package edu.ufp.inf.sd.rmi._advancewars.server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class GameFactoryImpl extends UnicastRemoteObject implements GameFactoryRI {

    private edu.ufp.inf.sd.rmi._04_diglib.server.DBMockup db;
    private HashMap<String, GameSessionImpl> sessions;
    public GameFactoryImpl() throws RemoteException {
        super();
        this.db = new edu.ufp.inf.sd.rmi._04_diglib.server.DBMockup();
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
    public GameSessionRI login(String username, String pwd) throws RemoteException {
        if(db.exists(username, pwd)){
            if(sessions.containsKey(username)){
                return sessions.get(username);
            }
            else{
                GameSessionImpl session = new GameSessionImpl(this,username);
                sessions.put(username, session);
                return session;
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

}