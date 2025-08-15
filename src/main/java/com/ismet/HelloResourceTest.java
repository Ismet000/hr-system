package com.ismet;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloResource.class); // Replace with your resource class
    }

    @Test
    public void testHelloEndpoint() {
        final String response = target("/hello").request().get(String.class);
        assertEquals("Hello, World!", response);
    }
}
