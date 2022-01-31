package rmi.cartstation;

import rmi.data.UpdateInfo;
import rmi.interfaces.IInfo;
import rmi.interfaces.INotification;
import rmi.interfaces.IRegistration;
import rmi.interfaces.IUpdate;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CartStation {
    protected final String name;
    protected final IRegistration serverRegistry;
    protected final IInfo serverInfo;
    protected final int capacity;
    protected int occupancy;
    protected final IUpdate serverUpdate;

    public CartStation(String cartStationName, int capacity, int port) throws NotBoundException, RemoteException {
        this.capacity = capacity;
        occupancy = 0;
        this.name = "cart_station;" + cartStationName + ";" + port;
        serverInfo = new IInfo() {
            @Override
            public String getName() throws RemoteException {
                return name;
            }
        };
        IInfo stubInfo = (IInfo) UnicastRemoteObject
                .exportObject(serverInfo, 0);
        Registry registryInfo = LocateRegistry.createRegistry(port);
        registryInfo.rebind("IInfo", stubInfo);

        Registry registry = LocateRegistry.getRegistry("localhost", 1101);
        serverRegistry = (IRegistration) registry
                .lookup("IRegistration");
        registry = LocateRegistry.getRegistry("localhost", 1102);
        serverUpdate = (IUpdate) registry
                .lookup("IUpdate");
    }

    public void register() throws RemoteException {
        serverRegistry.register(serverInfo);
    }

    public void unregister() throws RemoteException {
        serverRegistry.unregister(serverInfo);
    }

    public void addCart() throws RemoteException {
        if (occupancy != capacity)
            occupancy++;
        serverUpdate.update(new UpdateInfo(name, capacity, occupancy));
    }

    public void clearStation() throws RemoteException {
        occupancy = 0;
        serverUpdate.update(new UpdateInfo(name, capacity, occupancy));
    }
}
