package edu.ufp.inf.sd.rmi._advancewars.server;

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
    public String getOwnId() throws RemoteException {
        return gameFactoryImpl.getDb().getUser(user).getJwt();
    }

    @Override
    public ArrayList<GameLobby> getGames() throws RemoteException {
        return gameFactoryImpl.getDb().getGames();
    }

    @Override
    public GameLobby addGame(String map, String ID) throws RemoteException {
        //criar gamelobby e adicionar-se ao array de jogadores (com attach)
        GameLobby g = new GameLobby(map,ID);
        gameFactoryImpl.getDb().addGame(g);
        return g;
    }

    @Override
    public GameLobby joinGame(String g) throws RemoteException {
        //adicionar-se ao array de jogadores (com attach)
        return gameFactoryImpl.getDb().getGame(g);
    }

}
