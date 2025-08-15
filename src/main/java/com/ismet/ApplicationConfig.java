package com.ismet;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath("/api")  // this means your API base path is /api
public class ApplicationConfig extends Application {
}


