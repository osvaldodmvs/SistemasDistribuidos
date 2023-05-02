package edu.ufp.inf.sd.rmi._advancewars.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameSessionImpl extends UnicastRemoteObject implements GameSessionRI {

    private GameFactoryImpl gameFactoryImpl;
    private String user;

    public GameSessionImpl(GameFactoryImpl gameFactoryImpl, String username) throws RemoteException {
        super();
        this.gameFactoryImpl = gameFactoryImpl;
        this.user = username;
    }

    @Override
    public void logout() throws RemoteException {
        if (this.gameFactoryImpl.getSessions().containsKey(user))
            this.gameFactoryImpl.deleteSession(user);
    }

    @Override
    public String getuser() throws RemoteException {
        return this.user;
    }

}
