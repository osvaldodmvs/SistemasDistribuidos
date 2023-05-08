package edu.ufp.inf.sd.rmi._advancewars.server;


import edu.ufp.inf.sd.rmi._advancewars.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {

    private State subjectState;

    private final ArrayList<ObserverRI> observers = new ArrayList<>();

    public SubjectImpl() throws RemoteException {
        super();
    }

    public SubjectImpl(State subjectState) throws RemoteException {
        super();
        this.subjectState = subjectState;
    }

    @Override
    public void attach(ObserverRI obsRI) {
            observers.add(obsRI);
            //obsRI.update();
    }

    @Override
    public void detach(ObserverRI obsRI) {
            observers.remove(obsRI);

    }

    @Override
    public State getState() {
        return subjectState;
    }

    @Override
    public void setState(State state) {
        this.subjectState = state;
        notifyObservers();
    }

    public void notifyObservers(){
        for (ObserverRI o: observers) {
            try{
                o.update();
            } catch (RemoteException ex) {
                Logger.getLogger(SubjectImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
