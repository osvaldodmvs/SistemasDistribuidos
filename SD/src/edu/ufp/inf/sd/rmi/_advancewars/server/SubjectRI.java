package edu.ufp.inf.sd.rmi._advancewars.server;


import edu.ufp.inf.sd.rmi._advancewars.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface SubjectRI extends Remote {
    public void attach(ObserverRI obsRI) throws RemoteException;
    public void detach(ObserverRI obsRI) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State state) throws RemoteException;
    public void notifyObservers() throws RemoteException;
    public ArrayList<ObserverRI> getObservers() throws RemoteException;
}
