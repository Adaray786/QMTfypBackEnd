package com.fyp.resources;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:3000",
            "https://fyp.quran-mem-tool.pro"
    );

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String requestOrigin = requestContext.getHeaderString("Origin");

        // Check if Origin header exists and is in allowed origins
        if (requestOrigin != null && ALLOWED_ORIGINS.contains(requestOrigin)) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", requestOrigin);
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

            // Handle Preflight CORS Requests
            if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
                responseContext.setStatus(Response.Status.OK.getStatusCode());
                return; // Return immediately to prevent further processing
            }
        }
    }

}

