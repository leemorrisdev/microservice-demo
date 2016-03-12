package uk.co.leemorris.microservicedemo.common.consul;

import com.google.common.collect.Maps;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.model.health.ServiceHealth;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.config.DemoConfiguration;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Registers the service with consul and registers a health check to update service status.
 */
public class ConsulService {

    private Consul consul;
    private AgentClient agentClient;
    private DemoConfiguration demoConfiguration;
    private HealthClient healthClient;
    private final String serviceId;

    private Timer timer = new Timer();

    private Map<ServiceName, ServiceHealthCache> listeners = Maps.newHashMap();

    public ConsulService(ServiceName serviceName) {

        demoConfiguration = new DemoConfiguration();

        serviceId = Integer.valueOf(demoConfiguration.getConfig().getServices().get(serviceName).getId()).toString();
        int port = demoConfiguration.getConfig().getServices().get(serviceName).getPort();

        try {
            consul = Consul.builder().build();
            agentClient = consul.agentClient();
            healthClient = consul.healthClient();

            agentClient.register(port, 5L, serviceName.getDescription(), serviceId);

            timer.scheduleAtFixedRate(new PingConsul(), 0, 2000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void registerHealthyNodeListener(ServiceName serviceName,
                                            ConsulCache.Listener<HostAndPort, ServiceHealth> listener) {

        if(listeners.containsKey(serviceName)) {
            listeners.get(serviceName).addListener(listener);
        } else {
            ServiceHealthCache healthCache = ServiceHealthCache.newCache(healthClient, serviceName.getDescription());
            healthCache.addListener(listener);
            listeners.put(serviceName, healthCache);
            try {
                healthCache.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class PingConsul extends TimerTask {

        @Override
        public void run() {
            try {
                agentClient.pass(serviceId);
            } catch (NotRegisteredException e) {
                e.printStackTrace();
            }
        }
    }
}
