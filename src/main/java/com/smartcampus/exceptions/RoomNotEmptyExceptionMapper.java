/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author obaid
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Conflict");
        error.put("status", 409);
        error.put("message", e.getMessage());
        return Response.status(409)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
