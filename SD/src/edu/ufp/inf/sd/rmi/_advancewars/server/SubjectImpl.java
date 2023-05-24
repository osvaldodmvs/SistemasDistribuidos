package edu.ufp.inf.sd.rmi._advancewars.server;


import edu.ufp.inf.sd.rmi._advancewars.client.ObserverRI;
import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubjectImpl extends UnicastRemoteObject implements SubjectRI {


    private State subjectState;

    private final ArrayList<ObserverRI> observers = new ArrayList<>();

    private String token;

    private int currentPlayer;

    public SubjectImpl() throws RemoteException {
        super();
    }

    public SubjectImpl(State subjectState) throws RemoteException {
        super();
        this.token=null;
        this.subjectState = subjectState;
        this.currentPlayer = 0;
    }

    @Override
    public void attach(ObserverRI obsRI) throws RemoteException {
        observers.add(obsRI);
        //TODO : verificar se o jogo pode começar e se sim, notificar os observers
        if(obsRI.getGg().getNumPlayers()==obsRI.getGg().getMaxPlayers()){
            obsRI.getGg().setState("START");
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
    public void setState(State state) throws RemoteException {
        if(token==null){
            token=state.getId();
        }
        if(state.getId().compareTo(token)==0){
            if(state.getInfo().compareTo("END-TURN")==0){
                //System.out.println("FOUND END-TURN");
                //System.out.println("CURRENT PLAYER IS " + currentPlayer);
                currentPlayer++;
                if(currentPlayer==observers.size()){
                    currentPlayer=0;
                }
                //System.out.println("CURRENT PLAYER AFTER ++ IS " + currentPlayer);
                //System.out.println("CURRENT TOKEN IS " + token);
                token=observers.get(currentPlayer).getId();
                //System.out.println("STATE ------- MY TOKEN AFTER END TURN IS NOW " + token);
            }
            state.setId(token);
            //System.out.println("STATE ------- MY TOKEN IS NOW " + token);
            this.subjectState = state;
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
