package edu.ufp.inf.sd.rmi._advancewars.client;

import edu.ufp.inf.sd.rmi._advancewars.server.State;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private String id;

    private State lastObserverState;

    protected SubjectRI subjectRI;

    public ObserverImpl(String id, SubjectRI subjectRI) throws RemoteException {
        super();
        this.id = id;
        this.subjectRI = subjectRI;
        this.subjectRI.attach(this);
    }

    @Override
    public void update() {
        try {
            lastObserverState = subjectRI.getState();
        } catch (RemoteException ex) {
            Logger.getLogger(ObserverImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public State getLastObserverState() {
        System.out.println(lastObserverState);
        return lastObserverState;
    }
}