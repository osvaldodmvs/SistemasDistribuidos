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
            switch (lastObserverState.getInfo()) {
                case "START":
                    g.Start();
                    break;
                case "WAITING":
                    System.out.println("I AM STILL WAITING");
                    break;
                case "UP":
                    Game.updateGUI("UP");
                    break;
                case "DOWN":
                    Game.updateGUI("DOWN");
                    break;
                case "LEFT":
                    Game.updateGUI("LEFT");
                    break;
                case "RIGHT":
                    Game.updateGUI("RIGHT");
                    break;
                case "SELECT":
                    Game.updateGUI("SELECT");
                    break;
                case "CANCEL":
                    Game.updateGUI("CANCEL");
                    break;
                case "START-MENU":
                    Game.updateGUI("START-MENU");
                    break;
                default:
                    break;
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

    public State getLastObserverState() {
        System.out.println(lastObserverState);
        return lastObserverState;
    }
}