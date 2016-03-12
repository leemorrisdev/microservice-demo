package uk.co.leemorris.microservicedemo.common.config;

import uk.co.leemorris.microservicedemo.common.ServiceName;

import java.util.Map;

/**
 * Created by Lee on 05/03/2016.
 */
public class Configuration {

    private Map<ServiceName, Service> services;

    public Map<ServiceName, Service> getServices() {
        return services;
    }

    public void setServices(Map<ServiceName, Service> services) {
        this.services = services;
    }
}
