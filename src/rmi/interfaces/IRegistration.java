package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRegistration extends Remote {
    boolean register(IInfo info) throws RemoteException;

    boolean unregister(IInfo info) throws RemoteException;
}
