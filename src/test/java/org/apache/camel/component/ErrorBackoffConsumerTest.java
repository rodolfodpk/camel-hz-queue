package org.apache.camel.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.util.HzQueueTestSupport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ErrorBackoffConsumerTest extends HzQueueTestSupport {

    @Test
    public void backoffGivenError() throws Exception {

        MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.setExpectedCount(1);

        MockEndpoint mockBackoffGivenError = getMockEndpoint("mock:backoff-given-error");
        mockBackoffGivenError.setMinimumExpectedMessageCount(1);

        MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedBodiesReceived(1);

        template.sendBody("hz-queue://foo", 1);
        template.sendBody("hz-queue://foo", 2);

        assertThat(hzInstanceRegistry.getSize(), is(1));

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            public void configure() {

                onException(Exception.class)
                        .to("mock:error");

                from("hz-queue://foo?concurrentConsumers=10&backoffErrorThreshold=1&errorBackoffEventConsumer=mock:backoff-given-error")
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
