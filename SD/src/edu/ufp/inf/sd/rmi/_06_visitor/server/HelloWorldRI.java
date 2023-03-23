package edu.ufp.inf.sd.rmi._06_visitor.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface HelloWorldRI extends Remote {
    public void print(String msg) throws RemoteException;
}
