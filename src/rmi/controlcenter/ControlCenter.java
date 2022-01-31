package rmi.controlcenter;

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
import java.util.ArrayList;
import java.util.HashMap;

public class ControlCenter {
    private ArrayList<String> registeredWorkers;
    private ArrayList<String> registeredCartStations;
    private HashMap<String, Integer> registeredCartStationsStateHashMap;
    private HashMap<String, INotification> workersNotificationHashMap;

    public ControlCenter() throws RemoteException {
        registeredWorkers = new ArrayList<>();
        registeredCartStations = new ArrayList<>();
        registeredCartStationsStateHashMap = new HashMap<>();
        workersNotificationHashMap = new HashMap<>();

        IRegistration serverRegistration = new IRegistration() {
            @Override
            public boolean register(IInfo info) throws RemoteException {
                System.out.println(info.getName());
                if (info.getName().equals(""))
                    return false;
                if (info.getName().contains("worker")) {
                    registeredWorkers.add(info.getName());
                    Registry registry = LocateRegistry.getRegistry();
                    try {
                        String infoS = info.getName();
                        String[] temp = infoS.split(";");
                        int port = Integer.parseInt(temp[2]);
                        registry = LocateRegistry.getRegistry("localhost", port);
                        INotification notificationServer = (INotification) registry
                                .lookup(info.getName());
                        workersNotificationHashMap.put(info.getName(), notificationServer);
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                } else if (info.getName().contains("cart_station")) {
                    registeredCartStations.add(info.getName());
                    registeredCartStationsStateHashMap.put(info.getName(), 0);
                    return true;
                }
                return false;

            }

            @Override
            public boolean unregister(IInfo info) throws RemoteException {
                if (info.getName().equals(""))
                    return false;
                if (info.getName().contains("worker")) {

                    registeredWorkers.remove(info.getName());
                    workersNotificationHashMap.remove(info.getName());
                    return true;
                } else if (info.getName().contains("cart_station")) {
                    registeredCartStations.remove(info.getName());
                    registeredCartStationsStateHashMap.remove(info.getName());
                    return true;
                }
                return false;
            }
        };
        IRegistration stubRegistration = (IRegistration) UnicastRemoteObject
                .exportObject(serverRegistration, 0);
        Registry registryRegistration = LocateRegistry.createRegistry(1101);
        registryRegistration.rebind("IRegistration", stubRegistration);

        IUpdate serverUpdate = new IUpdate() {
            @Override
            public void update(UpdateInfo ui) throws RemoteException {
                if ((double) ui.occupancy / ui.capacity >= 0.85)
                    sendNotifications(ui);
            }
        };
        IUpdate stubUpdate = (IUpdate) UnicastRemoteObject
                .exportObject(serverUpdate, 0);
        Registry registryUpdate = LocateRegistry.createRegistry(1102);
        registryUpdate.rebind("IUpdate", stubUpdate);
    }

    protected void sendNotifications(UpdateInfo updateInfo) throws RemoteException {
        for (String registeredWorker : registeredWorkers) {
            workersNotificationHashMap.get(registeredWorker).notify(updateInfo);
        }
    }
}
