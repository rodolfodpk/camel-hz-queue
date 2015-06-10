package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hzqueue.utils.HzInstanceRegistry;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

public class HzProducerToHzConsumerTest extends HzQueueTestSupport {

    @Override
    public HzInstanceRegistry hzInstanceRegistry() {
        return new HzInstanceRegistry("default-hz-instance", hazelcastInstance4Test()).
                        with("hz2", hazelcastInstance4Test());
    }

    @Test @Ignore
    public void twoHzInstances() throws Exception {

        List<UUID> data = generate(10);

        MockEndpoint mock = getMockEndpoint("mock:result");

        mock.expectedMessageCount(data.size());

        data.forEach(uuid -> template.sendBody("hz-queue://foo", uuid));

        assertMockEndpointsSatisfied();

        assert(hzInstanceRegistry.getSize() == 2);

        assert(hzInstanceRegistry.getQueueFor(hzInstanceRegistry.defaultHzInstanceName, "foo").size() == 0);

        assert(hzInstanceRegistry.getQueueFor("hz2", "bar").size() == data.size());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo?poolingInterval=100&concurrentConsumers=10")
                  .to("mock:result")
                  .to("hz-queue://bar?hzInstanceName=hz2");
            }
        };
    }

}
