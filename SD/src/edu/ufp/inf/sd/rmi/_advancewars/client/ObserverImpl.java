package edu.ufp.inf.sd.rmi._advancewars.client;

import edu.ufp.inf.sd.rmi._advancewars.client.game.engine.Game;
import edu.ufp.inf.sd.rmi._advancewars.server.GameLobby;
import edu.ufp.inf.sd.rmi._advancewars.server.State;
import edu.ufp.inf.sd.rmi._advancewars.server.SubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private String id;

    private Game g;

    private GameLobby gg;

    private State lastObserverState;

    protected SubjectRI subjectRI;

    public ObserverImpl(String id, SubjectRI subjectRI, Game g, GameLobby gg) throws RemoteException {
        super();
        this.id = id;
        this.gg = gg;
        this.g = g;
        this.subjectRI = subjectRI;
        this.subjectRI.attach(this);
    }

    @Override
    public void update() {
        try {
            lastObserverState = subjectRI.getState();
            //TODO : implementar a verificaçao se = "START" com um if e o estado do jogo (teclas , implementar codigo de input handler)
            if(lastObserverState.getInfo().compareTo("START")==0){
                System.out.println("STARTING GAME");
                g.Start();
            }
            else if (lastObserverState.getInfo().compareTo("WAITING")!=0){
                Game.updateGUI(lastObserverState.getInfo());
            }
            else{
                System.out.println("WAITING FOR PLAYERS");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(ObserverImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public GameLobby getGg() {
        return gg;
    }

    @Override
    public Game getG() {
        return g;
    }

    @Override
    public void setG(Game g) {
        this.g = g;
    }

    public State getLastObserverState() {
        System.out.println(lastObserverState);
        return lastObserverState;
    }
}