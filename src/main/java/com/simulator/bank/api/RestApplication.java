package com.simulator.bank.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        // Scan API package
        packages("com.simulator.bank.api");
        // Enable Jackson JSON
        register(JacksonFeature.class);
    }
}
