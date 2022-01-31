package rmi.data;

import java.io.Serializable;

public class UpdateInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public String stationName;
    public int capacity;
    public int occupancy;

    public UpdateInfo(String stationName, int capacity, int occupancy) {
        this.stationName = stationName;
        this.capacity = capacity;
        this.occupancy = occupancy;
    }

    @Override
    public String toString() {
        return stationName + ";" + capacity + ";" + occupancy;
    }
}
