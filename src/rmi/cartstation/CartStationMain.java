package rmi.cartstation;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class CartStationMain {
    public static void main(String[] args) throws NotBoundException, RemoteException {

        new CartStationGui(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    }
}
