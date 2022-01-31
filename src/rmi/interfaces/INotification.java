package rmi.interfaces;

import java.rmi.Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.data.UpdateInfo;

public interface INotification extends Remote {
    public void notify(UpdateInfo ui) throws RemoteException;
}
