package rmi.worker;

import rmi.data.UpdateInfo;
import rmi.interfaces.IInfo;
import rmi.interfaces.INotification;
import rmi.interfaces.IRegistration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Worker {
    protected final String name;
    protected final IRegistration serverRegistry;
    protected final IInfo serverInfo;
    protected final ArrayList<UpdateInfo> updateInfoArrayList;

    public Worker(String workerName, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1101);
        serverRegistry = (IRegistration) registry
                .lookup("IRegistration");
        updateInfoArrayList = new ArrayList<>();
        this.name = "worker;" + workerName + ";" + port;
        serverInfo = new IInfo() {
            @Override
            public String getName() throws RemoteException {
                return name;
            }
        };
        IInfo stubInfo = (IInfo) UnicastRemoteObject
                .exportObject(serverInfo, 0);
        Registry registryInfo = LocateRegistry.createRegistry(1099);
        registryInfo.rebind("IInfo", stubInfo);

    }

    protected void updateInfoArrayListMethod(UpdateInfo updateInfo) {
        String temp = updateInfo.toString();
        String[] tempArr = temp.split(";");
        for (int i = 0; i < updateInfoArrayList.size(); i++) {
            if (updateInfoArrayList.get(i).stationName.equals(tempArr[0])) {
                updateInfoArrayList.set(i, updateInfo);
                return;
            }
        }
        updateInfoArrayList.add(updateInfo);
    }

    protected void register() throws RemoteException {
        serverRegistry.register(serverInfo);
    }

    protected void unregister() throws RemoteException {
        serverRegistry.unregister(serverInfo);
    }
}
