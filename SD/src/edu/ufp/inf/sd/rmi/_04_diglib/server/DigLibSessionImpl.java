package edu.ufp.inf.sd.rmi._04_diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DigLibSessionImpl extends UnicastRemoteObject implements DigLibSessionRI {

    private DigLibFactoryImpl diglibFacImpl;
    private String user;
    public DigLibSessionImpl(DigLibFactoryImpl diglibFacImpl, String username) throws RemoteException {
        super();
        this.diglibFacImpl = diglibFacImpl;
        this.user = username;
    }

    @Override
    public Book[] search(String title, String author) throws RemoteException {
        return this.diglibFacImpl.getDb().select(title,author);
    }

    @Override
    public void logout() throws RemoteException {
        if (this.diglibFacImpl.getSessions().containsKey(user))
            this.diglibFacImpl.deleteSession(user);
        }
    }
