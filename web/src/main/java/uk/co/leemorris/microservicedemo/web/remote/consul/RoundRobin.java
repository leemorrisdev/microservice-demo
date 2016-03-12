package uk.co.leemorris.microservicedemo.web.remote.consul;

import com.google.common.collect.Lists;
import uk.co.leemorris.microservicedemo.common.ServiceName;

import java.util.List;

/**
 * Cycles over a list of healthy nodes, allocating one request to each in order.
 */
public class RoundRobin {

    private final List<String> nodes = Lists.newArrayList();
    private ServiceName serviceName;
    private int currentServiceIndex = 0;

    public RoundRobin(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public void setHealthyNodes(List<String> healthyNodes) {

        synchronized(nodes) {
            nodes.clear();
            nodes.addAll(healthyNodes);

            // Reset index to save us bounds checking when getting next node.
            currentServiceIndex = 0;
        }

    }

    public String nextNode() {

        String nextNode;

        synchronized(nodes) {
            if(nodes.isEmpty()) {
                throw new RuntimeException("Service " + serviceName + " is unavailable");
            }

            if(nodes.size() == 1) {
                return nodes.get(0);
            }

            nextNode = nodes.get(currentServiceIndex);

            currentServiceIndex++;
        }

        return nextNode;
    }

}
