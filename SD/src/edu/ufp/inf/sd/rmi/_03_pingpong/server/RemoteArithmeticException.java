package edu.ufp.inf.sd.rmi._03_pingpong.server;

import java.rmi.RemoteException;

public class RemoteArithmeticException extends RemoteException {
    public RemoteArithmeticException() { super(); }

    public RemoteArithmeticException(String s) {
        super(s);
    }

    public RemoteArithmeticException(String s, Throwable cause) {
        super(s, cause);
    }
}
