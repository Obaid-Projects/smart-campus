/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.DataStore;
import com.smartcampus.exceptions.LinkedResourceNotFoundException;
import com.smartcampus.models.Room;
import com.smartcampus.models.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *
 * @author obaid
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> result = DataStore.sensors.values().stream()
                .filter(s -> type == null || s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
        return Response.ok(result).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(400)
                    .entity(Map.of("error", "Sensor ID is required"))
                    .build();
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(409)
                    .entity(Map.of("error", "Sensor with this ID already exists"))
                    .build();
        }
        if (sensor.getRoomId() == null || !DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "Room with ID '" + sensor.getRoomId() + "' does not exist.");
        }
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        DataStore.readings.put(sensor.getId(), new ArrayList<>());
        DataStore.sensors.put(sensor.getId(), sensor);
        return Response.status(201)
                .entity(Map.of("message", "Sensor created successfully", "sensor", sensor))
                .build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + sensorId))
                    .build();
        }
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }
        DataStore.sensors.remove(sensorId);
        DataStore.readings.remove(sensorId);
        return Response.ok(Map.of("message", "Sensor deleted successfully")).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
