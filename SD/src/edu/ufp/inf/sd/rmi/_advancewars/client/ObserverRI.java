package edu.ufp.inf.sd.rmi._advancewars.client;

import edu.ufp.inf.sd.rmi._advancewars.server.GameLobby;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends Remote {
    public void update() throws RemoteException;

    public String getId() throws RemoteException;

    public void setId(String id) throws RemoteException;

    public GameLobby getGg() throws RemoteException;

    }
