package rmi.cartstation;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CartStationGui extends CartStation {

    private final JTextField occupancyTextField;
    private final JButton registerButton;
    private final JButton unregisterButton;
    private final JButton addCartButton;
    private final JButton clearStationButton;

    public CartStationGui(String name, int capacity, int port) throws NotBoundException, RemoteException {
        super(name, capacity, port);
        //construct components
        JFrame frame = new JFrame("cart station");
        JLabel occupancyLabel = new JLabel("occupancy :");
        occupancyTextField = new JTextField(5);
        JLabel capacityLabel = new JLabel("capacity :");
        JTextField capacityTextField = new JTextField(5);
        registerButton = new JButton("register");
        unregisterButton = new JButton("unregister");
        addCartButton = new JButton("add cart");
        clearStationButton = new JButton("clear station");

        //set components properties
        addCartButton.setEnabled(false);
        clearStationButton.setEnabled(false);

        occupancyTextField.setEnabled(false);
        unregisterButton.setEnabled(false);

        capacityTextField.setText(String.valueOf(capacity));
        capacityTextField.setEditable(false);

        occupancyTextField.setText(String.valueOf(occupancy));
        occupancyTextField.setEditable(false);

        registerButton.addActionListener(e -> registerGui());
        unregisterButton.addActionListener(e -> unregisterGui());
        addCartButton.addActionListener(e -> addCartGui());
        clearStationButton.addActionListener(e -> clearStationGui());

        //adjust size and set layout
        frame.setPreferredSize(new Dimension(404, 194));
        frame.setLayout(null);
        frame.setResizable(false);

        //add components
        frame.add(occupancyLabel);
        frame.add(occupancyTextField);
        frame.add(capacityLabel);
        frame.add(capacityTextField);
        frame.add(registerButton);
        frame.add(unregisterButton);
        frame.add(addCartButton);
        frame.add(clearStationButton);

        //set component bounds (only needed by Absolute Positioning)
        occupancyLabel.setBounds(205, 5, 100, 25);
        occupancyTextField.setBounds(285, 5, 100, 25);
        capacityLabel.setBounds(205, 35, 80, 25);
        capacityTextField.setBounds(285, 35, 100, 25);
        registerButton.setBounds(10, 15, 100, 25);
        unregisterButton.setBounds(10, 50, 100, 25);
        addCartButton.setBounds(160, 120, 100, 25);
        clearStationButton.setBounds(160, 80, 130, 25);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void registerGui() {
        EventQueue.invokeLater(() -> {
            try {
                register();
                registerButton.setEnabled(false);
                unregisterButton.setEnabled(true);
                addCartButton.setEnabled(true);
                clearStationButton.setEnabled(true);
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
                addCartButton.setEnabled(false);
                clearStationButton.setEnabled(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });
    }

    private void addCartGui() {
        EventQueue.invokeLater(() -> {
            try {
                addCart();
                occupancyTextField.setText(String.valueOf(occupancy));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void clearStationGui() {
        EventQueue.invokeLater(() -> {
            try {
                clearStation();
                occupancyTextField.setText(String.valueOf(occupancy));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
