package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ErrorBackoffConsumerTest extends HzQueueTestSupport {

    @Test
    public void fromDefaultHzQueueFoo2Hz2QueueBar() throws Exception {

        MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.setExpectedCount(1);

        MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.setMinimumExpectedMessageCount(1);

        template.sendBody("hz-queue://foo", 1);
        template.sendBody("hz-queue://foo", 2);

        assertThat(hzInstanceRegistry.getSize(), is(1));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo?concurrentConsumers=10&backoffErrorThreshold=1&errorBackoffEventConsumer=mock:error")
                        .process(exchange -> {
                            if (new Integer(1).equals(exchange.getIn().getBody(Integer.class))) {
                                throw new RuntimeException("error for 1");
                            }
                        })
                  .to("mock:result");
            }
        };
    }

}
