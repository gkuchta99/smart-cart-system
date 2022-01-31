package rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.data.UpdateInfo;

public interface IUpdate extends Remote {
    public void update(UpdateInfo ui) throws RemoteException;
}
