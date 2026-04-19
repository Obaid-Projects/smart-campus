/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import com.smartcampus.models.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author obaid
 */
public class DataStore {

    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    public static final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    static {
        // Sample rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        // Sample sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 21.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");
        Sensor s3 = new Sensor("OCC-001", "Occupancy", "MAINTENANCE", 0.0, "LIB-301");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);

        // Link sensors to rooms
        r1.getSensorIds().add("TEMP-001");
        r1.getSensorIds().add("OCC-001");
        r2.getSensorIds().add("CO2-001");

        // Empty readings lists for each sensor
        readings.put("TEMP-001", new ArrayList<>());
        readings.put("CO2-001", new ArrayList<>());
        readings.put("OCC-001", new ArrayList<>());
    }
}
