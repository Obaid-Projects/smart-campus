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
import java.util.logging.Logger;

/**
 *
 * @author obaid
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable e) {
        LOGGER.severe("Unexpected error: " + e.getMessage());
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("status", 500);
        error.put("message", "An unexpected error occurred. Please try again later.");
        return Response.status(500)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
