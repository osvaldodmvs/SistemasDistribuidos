package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

//metodos a chamar quando a sessao esta criada
public class GameSessionImpl extends UnicastRemoteObject implements GameSessionRI {

    private GameFactoryImpl gameFactoryImpl;
    private String user;

    //private String token; e criar metodo de create token e refresh token

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
    public ArrayList<Game> getGames() throws RemoteException {
        return gameFactoryImpl.getDb().getGames();
    }
}
