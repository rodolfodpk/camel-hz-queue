package org.apache.camel.component.hzqueue;

import com.hazelcast.core.IQueue;
import org.apache.camel.Exchange;
import org.apache.camel.component.hzqueue.utils.HzComponentHelper;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * The HzQueue producer.
 */
public class HzQueueProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(HzQueueProducer.class);
    private HzQueueEndpoint endpoint;
    private final IQueue<Object> queue;

    public HzQueueProducer(HzQueueEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
        this.queue = endpoint.getQueue();
    }

    public void process(Exchange exchange) throws Exception {

        final Object body = exchange.getIn().getBody();

        try {
            queue.offer(body, endpoint.getPoolingInterval() - 100, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            exchange.setException(e);
            log.error("on queue.offer operation: ", e);
        }

        // finally copy headers
        HzComponentHelper.copyHeaders(exchange);
    }

}
