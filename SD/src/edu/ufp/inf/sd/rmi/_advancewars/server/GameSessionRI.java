package edu.ufp.inf.sd.rmi._advancewars.server;


import java.rmi.Remote;
import java.rmi.RemoteException;


public interface GameSessionRI extends Remote {
    public void logout () throws RemoteException;
}
