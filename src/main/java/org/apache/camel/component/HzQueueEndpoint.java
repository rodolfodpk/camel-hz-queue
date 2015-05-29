package org.apache.camel.component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.utils.HzInstanceRegistry;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a HzQueue endpoint.
 */
@UriEndpoint(scheme = "hz-queue", title = "HzQueue", syntax="hz-queue:name", consumerClass = HzQueueConsumer.class, label = "HzQueue")
public class HzQueueEndpoint extends DefaultEndpoint {

    private final HazelcastInstance hzInstance;
    private final IQueue<Object> queue;

    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "hz-instance-0")
    private String hzInstanceName = HzInstanceRegistry.defaultHzInstanceName;
    @UriParam(defaultValue = "10")
    private int concurrentConsumers = 1;
    @UriParam(defaultValue = "10")
    private int backoffIdleThreshold = 3;
    @UriParam(defaultValue = "10")
    private int backoffErrorThreshold = 3;
    @UriParam(defaultValue = "5")
    private int backoffMultiplier = 3;
    @UriParam(defaultValue = "1000")
    private int poolingInterval = 1000;
    @UriParam(defaultValue = "", description = "endpoint consuming backoff given idle event")
    private String idleBackoffEventConsumer = null;
    @UriParam(defaultValue = "", description = "endpoint consuming backoff given error event")
    private String errorBackoffEventConsumer = null;

    public HzQueueEndpoint(String uri, HzQueueComponent component, HazelcastInstance hzInstance, String targetQueue) {
        super(uri, component);
        this.hzInstance = hzInstance;
        this.queue = hzInstance.getQueue(targetQueue);
    }

    public Producer createProducer() throws Exception {
        return new HzQueueProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new HzQueueConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setConcurrentConsumers(int concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public int getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public int getBackoffErrorThreshold() {
        return backoffErrorThreshold;
    }

    public int getBackoffIdleThreshold() {
        return backoffIdleThreshold;
    }

    public void setBackoffIdleThreshold(int backoffIdleThreshold) {
        this.backoffIdleThreshold = backoffIdleThreshold;
    }

    public void setBackoffErrorThreshold(int backoffErrorThreshold) {
        this.backoffErrorThreshold = backoffErrorThreshold;
    }

    public int getBackoffMultiplier() {
        return backoffMultiplier;
    }

    public void setBackoffMultiplier(int backoffMultiplier) {
        this.backoffMultiplier = backoffMultiplier;
    }

    public int getPoolingInterval() {
        return poolingInterval;
    }

    public void setPoolingInterval(int poolingInterval) {
        this.poolingInterval = poolingInterval;
    }

    public String getHzInstanceName() {
        return hzInstanceName;
    }

    public void setHzInstanceName(String hzInstanceName) {
        this.hzInstanceName = hzInstanceName;
    }

    public HazelcastInstance getHzInstance() {
        return hzInstance;
    }

    public IQueue<Object> getQueue() {
        return queue;
    }

    public String getIdleBackoffEventConsumer() {
        return idleBackoffEventConsumer;
    }

    public void setIdleBackoffEventConsumer(String idleBackoffEventConsumer) {
        this.idleBackoffEventConsumer = idleBackoffEventConsumer;
    }

    public String getErrorBackoffEventConsumer() {
        return errorBackoffEventConsumer;
    }

    public void setErrorBackoffEventConsumer(String errorBackoffEventConsumer) {
        this.errorBackoffEventConsumer = errorBackoffEventConsumer;
    }
}
