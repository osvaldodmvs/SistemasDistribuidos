package edu.ufp.inf.sd.rmi._advancewars.server;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameFactoryRI extends Remote {
    public boolean register (String username, String pwd) throws RemoteException;
    public GameSessionRI login (String username, String pwd) throws RemoteException;
}