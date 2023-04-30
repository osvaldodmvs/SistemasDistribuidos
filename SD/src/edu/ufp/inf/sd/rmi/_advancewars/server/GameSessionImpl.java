package edu.ufp.inf.sd.rmi._advancewars.server;



import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameSessionImpl extends UnicastRemoteObject implements GameSessionRI {

    private GameFactoryImpl gameFactoryImpl;
    private String user;
    public GameSessionImpl(GameFactoryImpl diglibFacImpl, String username) throws RemoteException {
        super();
        this.gameFactoryImpl = diglibFacImpl;
        this.user = username;
    }

    @Override
    public void logout() throws RemoteException {
        if (this.gameFactoryImpl.getSessions().containsKey(user))
            this.gameFactoryImpl.deleteSession(user);
        }
    }
