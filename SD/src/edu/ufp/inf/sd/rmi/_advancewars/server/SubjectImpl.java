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

    private String token;

    public SubjectImpl() throws RemoteException {
        super();
    }

    public SubjectImpl(State subjectState) throws RemoteException {
        super();
        this.token=null;
        this.subjectState = subjectState;
    }

    @Override
    public void attach(ObserverRI obsRI) throws RemoteException {
        observers.add(obsRI);
        //TODO : verificar se o jogo pode começar e se sim, notificar os observers
        if(obsRI.getGg().getNumPlayers()==obsRI.getGg().getMaxPlayers()){
            System.out.println("SETTING GAMELOBBY STATE TO START");
            obsRI.getGg().setState("START");
            System.out.println("SETTING SUBJECT STATE TO START");
            obsRI.getGg().getSubject().setState(new State(obsRI.getGg().getId(),"START"));
        }
        obsRI.update();
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
        if(token==null){
            token=state.getId();
        }
        if(state.getId().compareTo(token)==0){
            System.out.println("STATE ------- MY STATE WAS " + subjectState.getInfo());
            this.subjectState = state;
            System.out.println("STATE ------- MY STATE HAS BEEN CHANGED TO " + state.getInfo());
            notifyObservers();
        }
    }

    @Override
    public void notifyObservers(){
        for (ObserverRI o: observers) {
            try{
                o.update();
            } catch (RemoteException ex) {
                Logger.getLogger(SubjectImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<ObserverRI> getObservers() throws RemoteException{
        return observers;
    }

}
