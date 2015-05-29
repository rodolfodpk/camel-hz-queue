package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TwoHzInstancesTest extends HzQueueTestSupport {

    @Test
    public void fromDefaultHzQueueFoo2Hz2QueueBar() throws Exception {

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

        final AtomicLong i = new AtomicLong(0);

        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo?poolingInterval=100&concurrentConsumers=10")
                  .to("mock:result")
                  .to("hz-queue://bar?hzInstanceName=hz2");
            }
        };
    }

    private List<UUID> generate(int dataSetSize) {
        List<UUID> result = new ArrayList<>(dataSetSize);
        for (int i=0; i< dataSetSize; i++){
            result.add(UUID.randomUUID());
            // System.out.println(result.get(i));
        }
        return result;
    }
}
