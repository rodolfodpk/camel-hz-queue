package org.apache.camel.component.utils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

import java.util.HashMap;
import java.util.Map;

/**
 * Component will look for an instance of this class in order to find the target HazelcastInstance
 */
public class HzInstanceRegistry {

    public final static String defaultHzInstanceName = "hz-queue-instance-0";

    private final Map<String, HazelcastInstance> hzInstanceMap;

    public HzInstanceRegistry(String defaultHzInstanceName, HazelcastInstance hazelcastInstance) {
        this.hzInstanceMap = new HashMap<>();
        hzInstanceMap.put(defaultHzInstanceName, hazelcastInstance);
    }

    public HazelcastInstance getHzFor(String hzInstanceName) {
        return hzInstanceMap.get(hzInstanceName);
    }

    public IQueue<Object> getQueueFor(String hzInstanceName, String queueName) {
        return hzInstanceMap.get(hzInstanceName).getQueue(queueName);
    }

    public boolean hasOnly(String defaultHzInstanceName) {
        return hzInstanceMap.get(defaultHzInstanceName) != null && hzInstanceMap.size() ==1 ;
    }

    public int getSize() {
        return hzInstanceMap.size();
    }

    public void register(String hzInstanceName, HazelcastInstance hz) {
        hzInstanceMap.put(hzInstanceName, hz);
    }
}
