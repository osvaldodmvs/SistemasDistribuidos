package edu.ufp.inf.sd.rmi._advancewars.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends Remote {
    public void update() throws RemoteException;
}
