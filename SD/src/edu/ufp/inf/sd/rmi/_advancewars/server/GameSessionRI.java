package edu.ufp.inf.sd.rmi._advancewars.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameSessionRI extends Remote {
    public void logout() throws RemoteException;

    public String getOwnId() throws RemoteException;

    public ArrayList<GameLobby> getGames() throws RemoteException;

    public GameLobby addGame(String map, String ID, int commander) throws RemoteException;

    public GameLobby joinGame(String g, int commander) throws RemoteException;

    public GameLobby getGameIDfromLobby(String game) throws RemoteException;
}
