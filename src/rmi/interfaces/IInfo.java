package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IInfo extends Remote {
    String getName() throws RemoteException;
}
