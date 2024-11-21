package com.fyp.auth;

import io.dropwizard.auth.AuthFilter;
import com.fyp.cli.User;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
@Priority(1000)
public class JWTFilter extends AuthFilter<String, User> {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String header = containerRequestContext.getHeaders().getFirst("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            this.authenticate(containerRequestContext, null, "BEARER");
            return;
        }

        String token = header.substring("Bearer".length()).trim();

        if (!this.authenticate(containerRequestContext, token, "BEARER")) {
            throw new WebApplicationException(this.unauthorizedHandler.buildResponse(this.prefix, this.realm));
        }
    }

    public static class Builder extends AuthFilter.AuthFilterBuilder<String, User, JWTFilter> {
        public Builder() {
        }
        protected JWTFilter newInstance() {
            return new JWTFilter();
        }
    }
}
