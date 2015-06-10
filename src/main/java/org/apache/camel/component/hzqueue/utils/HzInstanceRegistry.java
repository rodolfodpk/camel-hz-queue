package org.apache.camel.component.hzqueue.utils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Component will look for an instance of this class in order to find the target HazelcastInstance
 */
public class HzInstanceRegistry {

    public final static String DEFAULT_INSTANCE_NAME  = "default-hz-instance" ;

    public final String defaultHzInstanceName ;

    private final Map<String, HazelcastInstance> hzInstanceMap;

    public HzInstanceRegistry(HazelcastInstance hazelcastInstance) {
        this.hzInstanceMap = new HashMap<>();
        this.defaultHzInstanceName = DEFAULT_INSTANCE_NAME;
        hzInstanceMap.put(defaultHzInstanceName, hazelcastInstance);
    }

    public HzInstanceRegistry(String defaultHzInstanceName, HazelcastInstance hazelcastInstance) {
        this.hzInstanceMap = new HashMap<>();
        this.defaultHzInstanceName = defaultHzInstanceName;
        hzInstanceMap.put(defaultHzInstanceName, hazelcastInstance);
    }

    public HzInstanceRegistry with(String defaultHzInstanceName, HazelcastInstance hazelcastInstance) {
        hzInstanceMap.put(defaultHzInstanceName, hazelcastInstance);
        return this;
    }

    public Optional<HazelcastInstance> getHzFor(String hzInstanceName) {
        return hzInstanceMap.get(hzInstanceName) == null ? Optional.empty() : Optional.of(hzInstanceMap.get(hzInstanceName));
    }

    public IQueue<Object> getQueueFor(String hzInstanceName, String queueName) {
        return hzInstanceMap.get(hzInstanceName).getQueue(queueName);
    }

    public int getSize() {
        return hzInstanceMap.size();
    }

    public void register(String hzInstanceName, HazelcastInstance hz) {
        hzInstanceMap.put(hzInstanceName, hz);
    }
}
