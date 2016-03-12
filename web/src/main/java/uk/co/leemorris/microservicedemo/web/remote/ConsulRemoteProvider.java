package uk.co.leemorris.microservicedemo.web.remote;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.net.HostAndPort;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.orbitz.consul.cache.ConsulCache;
import com.orbitz.consul.model.health.ServiceHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import uk.co.leemorris.microservicedemo.common.ProviderType;
import uk.co.leemorris.microservicedemo.common.ServiceName;
import uk.co.leemorris.microservicedemo.common.consul.ConsulService;
import uk.co.leemorris.microservicedemo.web.remote.consul.RoundRobin;

import java.util.List;
import java.util.Map;

/**
 * Routes messages using consul for a list of healthy services.
 */
public class ConsulRemoteProvider implements RemoteProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ConsulRemoteProvider.class);

    private ConsulService consulService;

    private Map<ServiceName, RoundRobin> schedulers = Maps.newConcurrentMap();

    public ConsulRemoteProvider(ConsulService consulService) {
        this.consulService = consulService;

        initialiseListeners();
    }

    @Override
    public Observable<String> get(ServiceName service, String url) {

        String node = schedulers.get(service).nextNode();

        String fullUrl = "http://" + node + url;

        return Observable.from(Unirest.get(fullUrl).asStringAsync()).map(HttpResponse::getBody);
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.CONSUL;
    }

    private void initialiseListeners() {

        for(ServiceName serviceName : ServiceName.values()) {
            schedulers.put(serviceName, new RoundRobin(serviceName));

            LOG.info("Registering service listener for {}", serviceName);

            consulService.registerHealthyNodeListener(serviceName, newValues -> {

                List<String> healthyNodes = Lists.newArrayList();

                for(HostAndPort node : newValues.keySet()) {
                    healthyNodes.add(node.getHostText() + ":" + node.getPort());
                }

                LOG.info("{} : {}", serviceName, healthyNodes);

                schedulers.get(serviceName).setHealthyNodes(healthyNodes);
            });
        }
    }
}
