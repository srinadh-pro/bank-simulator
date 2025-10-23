package com.simulator.bank;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class BankApplication extends ResourceConfig {
    public BankApplication() {
        // Scan your @Path classes
        packages("com.simulator.bank.api");
    }
}
