/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.DataStore;
import com.smartcampus.exceptions.RoomNotEmptyException;
import com.smartcampus.models.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 *
 * @author obaid
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public Response getAllRooms() {
        return Response.ok(DataStore.rooms.values()).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(400)
                    .entity(Map.of("error", "Room ID is required"))
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(409)
                    .entity(Map.of("error", "Room with this ID already exists"))
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(201)
                .entity(Map.of("message", "Room created successfully", "room", room))
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found: " + roomId))
                    .build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found: " + roomId))
                    .build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room " + roomId
                    + " cannot be deleted because it still has sensors assigned to it.");
        }
        DataStore.rooms.remove(roomId);
        return Response.ok(Map.of("message", "Room deleted successfully")).build();
    }
}
