package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DigLibFactoryRI extends Remote {
    public boolean register (String username, String pwd) throws RemoteException;
    public DigLibSessionRI login (String username, String pwd) throws RemoteException;
}