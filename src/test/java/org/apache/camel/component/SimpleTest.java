package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;

public class SimpleTest extends HzQueueTestSupport {

    @Test
    public void simple() throws Exception {

        MockEndpoint mock = getMockEndpoint("mock:result");

        List<UUID> data = generate(2);
        mock.expectedMinimumMessageCount(data.size());

        data.forEach(uuid -> template.sendBody("hz-queue://foo", uuid));

        assertThat(hzInstanceRegistry.getSize(), is(1));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo?concurrentConsumers=10")
                  .to("hz-queue://bar?")
                  .to("mock:result");
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
