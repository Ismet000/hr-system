package com.ismet.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/api")
public class JaxRsConfig extends Application {
}


//
//@ApplicationPath("/api")
//public class JaxRsConfig extends ResourceConfig {
//
//    public JaxRsConfig(){
//        packages("com.ismet.resources");
//    }
//}
