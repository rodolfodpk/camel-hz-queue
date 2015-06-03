package org.apache.camel.component.hzqueue;

import com.hazelcast.core.IQueue;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.hzqueue.utils.CustomScheduledPollConsumer;
import org.apache.camel.component.hzqueue.utils.HzComponentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The HzQueue consumer.
 */
public class HzQueueConsumer extends CustomScheduledPollConsumer {

    private static final Logger log = LoggerFactory.getLogger(HzQueueConsumer.class);

    private final HzQueueEndpoint endpoint;
    private final IQueue<Object> queue;

    public HzQueueConsumer(HzQueueEndpoint endpoint, Processor processor) {

        super(endpoint, processor);

        setBackoffErrorThreshold(endpoint.getBackoffErrorThreshold());
        setBackoffIdleThreshold(endpoint.getBackoffIdleThreshold());
        setBackoffMultiplier(endpoint.getBackoffMultiplier());
        setDelay(endpoint.getPoolingInterval());
        setScheduledExecutorService(Executors.newScheduledThreadPool(endpoint.getConcurrentConsumers()));

        setIdleBackoffEventConsumer(endpoint.getIdleBackoffEventConsumer() == null || endpoint.getIdleBackoffEventConsumer().equals("") ?
                Optional.<String>empty() :  Optional.of(endpoint.getIdleBackoffEventConsumer()));

        setErrorBackoffEventConsumer(endpoint.getErrorBackoffEventConsumer() == null || endpoint.getErrorBackoffEventConsumer().equals("") ?
                Optional.<String>empty() :  Optional.of(endpoint.getErrorBackoffEventConsumer()));

        this.endpoint = endpoint;
        this.queue = endpoint.getQueue();
    }

    @Override
    protected int poll() throws Exception {

        final Exchange exchange = endpoint.createExchange();

        final Object obj = queue.poll(endpoint.getPoolingInterval()-100, TimeUnit.MILLISECONDS);

        if (obj==null) {
            return 0; // number of messages polled
        }

        exchange.getIn().setBody(obj);

        // finally copy headers
        HzComponentHelper.copyHeaders(exchange);

        // send message to next processor in the route
        getProcessor().process(exchange);

        if (exchange.getException()!=null) {
            throw new RuntimeException(exchange.getException());
        }

        return 1;
    }

}
