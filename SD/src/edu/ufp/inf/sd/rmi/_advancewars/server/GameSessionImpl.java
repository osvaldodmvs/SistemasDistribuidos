package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._advancewars.client.ObserverRI;

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
    public GameLobby addGame(String map, String ID, int commander) throws RemoteException {
        //criar gamelobby e adicionar-se ao array de jogadores (com attach)
        GameLobby g = new GameLobby(map,ID);
        gameFactoryImpl.getDb().addGame(g);
        g.getCommanders().add(commander);
        System.out.println("NEW -> Map is " + g.getMap());
        return g;
    }

    @Override
    public GameLobby joinGame(String game, int commander) throws RemoteException {
        //adicionar-se ao array de jogadores (com attach)
        GameLobby g = gameFactoryImpl.getDb().getGame(game);
        if(g.getNumPlayers()==g.getMaxPlayers()){
            System.out.println("Game is full");
            return null;
        }
        System.out.println("Game had " + g.getNumPlayers() + " players");
        g.setNumPlayers(g.getNumPlayers()+1);
        System.out.println("Game has " + g.getNumPlayers() + " players");
        System.out.println("JOIN -> Map is " + g.getMap());
        g.getCommanders().add(commander);
        return g;
    }

    @Override
    public GameLobby getGameIDfromLobby(String game) throws RemoteException {
        System.out.println("Looking for lobby which game with ID: " + game);
        for (GameLobby g : gameFactoryImpl.getDb().getGames()) {
            for (ObserverRI oi : g.getSubject().getObservers()) {
                if (oi.getId().compareTo(game)==0) {
                    System.out.println("Game found with ID: " + g.getId());
                    return g;
                }
            }
        }
        return null;
    }
}
