package org.apache.camel.component.hzqueue;

import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.hzqueue.utils.HzInstanceRegistry;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;
import java.util.Optional;

/**
 * Represents the component that manages {@link HzQueueEndpoint}.
 */
public class HzQueueComponent extends UriEndpointComponent {

    private HazelcastInstance hz;
    
    public HzQueueComponent() {
        super(HzQueueEndpoint.class);
    }

    public HzQueueComponent(CamelContext context) {
        super(context, HzQueueEndpoint.class);
    }

    String hzInstanceName(Map<String, Object> parameters) {
        return (String) parameters.get("hzInstanceName");
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        final String hzInstanceName = hzInstanceName(parameters) ==null ? "default-hz-instance" : hzInstanceName(parameters);

        final String queueName = remaining.substring(0, Math.max(remaining.indexOf("&"), remaining.length()));

        final HzInstanceRegistry hzInstanceRegistry =
                getCamelContext().getRegistry().findByType(HzInstanceRegistry.class).stream().findFirst().get();

        if (hzInstanceRegistry == null) {
            throw new IllegalStateException("You must have a HzInstanceRegistry instance within Camel registry !");
        }

        if (!hzInstanceRegistry.getHzFor(hzInstanceName).isPresent()) {
            throw new IllegalStateException("You must have a Hazelcast instance named " + hzInstanceName + " within HzInstanceRegistry!");
        }

       final Optional<HazelcastInstance> hz = hzInstanceRegistry.getHzFor(hzInstanceName);

       final HzQueueEndpoint endpoint =
                new HzQueueEndpoint(uri, this, hzInstanceRegistry.getHzFor(hzInstanceName).get(), queueName);

        setProperties(endpoint, parameters);
        return endpoint;
    }

}
