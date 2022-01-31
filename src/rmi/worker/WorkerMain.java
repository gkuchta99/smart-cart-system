package rmi.worker;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class WorkerMain {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        new WorkerGui(args[0], Integer.parseInt(args[1]));
    }
}
