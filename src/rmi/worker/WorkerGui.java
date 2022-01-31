package rmi.worker;

import rmi.data.UpdateInfo;
import rmi.interfaces.INotification;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class WorkerGui extends Worker {
    private JButton registerButton;
    private JButton unregisterButton;
    private JButton clearStation;
    private JList stationJlist;
    private JFrame frame;

    public WorkerGui(String workerName, int port) throws NotBoundException, RemoteException {
        super(workerName, port);
        //construct preComponents
        frame = new JFrame("worker");
        String[] stationJlistItems = {"Item 1", "Item 2", "Item 3"};

        //construct components
        registerButton = new JButton("register");
        registerButton.addActionListener(e -> registerGui());
        unregisterButton = new JButton("unregister");
        unregisterButton.addActionListener(e -> unregisterGui());
        clearStation = new JButton("clear");
        stationJlist = new JList(stationJlistItems);

        //adjust size and set layout
        frame.setPreferredSize(new Dimension(460, 230));
        frame.setLayout(null);
        frame.setResizable(false);

        //add components
        frame.add(registerButton);
        frame.add(unregisterButton);
        frame.add(clearStation);
        frame.add(stationJlist);

        //set component bounds (only needed by Absolute Positioning)
        registerButton.setBounds(20, 60, 100, 20);
        unregisterButton.setBounds(135, 60, 100, 20);
        unregisterButton.setEnabled(false);
        clearStation.setBounds(280, 150, 140, 20);
        stationJlist.setBounds(300, 15, 95, 115);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        INotification serverNotification = new INotification() {
            @Override
            public void notify(UpdateInfo ui) throws RemoteException {
                JOptionPane.showMessageDialog(frame,
                        ui.stationName + " actual state: " + (double) ui.occupancy / ui.capacity * 100 + "%",
                        ui.stationName,
                        JOptionPane.ERROR_MESSAGE);

            }
        };
        INotification stubNotification = (INotification) UnicastRemoteObject
                .exportObject(serverNotification, 0);
        Registry registryNotification = LocateRegistry.createRegistry(port);
        registryNotification.rebind(name, stubNotification);
    }

    private void registerGui() {
        EventQueue.invokeLater(() -> {
            try {
                register();
                registerButton.setEnabled(false);
                unregisterButton.setEnabled(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void unregisterGui() {
        EventQueue.invokeLater(() -> {
            try {
                unregister();
                registerButton.setEnabled(true);
                unregisterButton.setEnabled(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });
    }

}
