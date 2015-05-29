package org.apache.camel.component;

import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.utils.HzConfigRegistry;
import org.apache.camel.component.utils.HzInstanceHelper;
import org.apache.camel.component.utils.HzInstanceRegistry;
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

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        final HzInstanceHelper hzInstanceHelper = getHzInstanceHelper(parameters);
        final String queueName = remaining.substring(0, Math.max(remaining.indexOf("&"), remaining.length()));
        final HzQueueEndpoint endpoint = new HzQueueEndpoint(uri, this, hzInstanceHelper.findHzInstance(), queueName);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    private HzInstanceHelper getHzInstanceHelper(Map<String, Object> parameters) {

        final String hzInstanceName = (String) parameters.get("hzInstanceName");
        final Optional<HzInstanceRegistry> hzInstanceRegistry =
                getCamelContext().getRegistry().findByType(HzInstanceRegistry.class).stream().findFirst();
        final Optional<HzConfigRegistry> hzConfigRegistry =
                getCamelContext().getRegistry().findByType(HzConfigRegistry.class).stream().findFirst();
        return new HzInstanceHelper(hzInstanceName != null ? hzInstanceName : HzInstanceRegistry.defaultHzInstanceName, hzInstanceRegistry, hzConfigRegistry);
    }

}
