/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 *
 * @author obaid
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) {
        LOGGER.info("Incoming request: "
                + requestContext.getMethod() + " "
                + requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) {
        LOGGER.info("Outgoing response: "
                + responseContext.getStatus());

    }
}