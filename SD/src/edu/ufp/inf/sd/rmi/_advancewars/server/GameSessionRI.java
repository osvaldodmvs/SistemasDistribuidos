package edu.ufp.inf.sd.rmi._advancewars.server;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameSessionRI extends Remote {
    public void logout () throws RemoteException;
    public ArrayList<Game> getGames () throws RemoteException;
}
