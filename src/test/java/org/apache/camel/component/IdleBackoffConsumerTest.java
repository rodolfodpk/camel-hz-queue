package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class IdleBackoffConsumerTest extends HzQueueTestSupport {

    @Test
    public void fromDefaultHzQueueFoo2Hz2QueueBar() throws Exception {

        MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.setExpectedCount(0);

        MockEndpoint mockIdle = getMockEndpoint("mock:idle");
        mockIdle.setMinimumExpectedMessageCount(1);

        assertThat(hzInstanceRegistry.getSize(), is(1));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo?concurrentConsumers=10&backoffIdleThreshold=1&idleBackoffEventConsumer=mock:idle")
                  .to("mock:result");
            }
        };
    }

}
