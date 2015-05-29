package org.apache.camel.component.hzqueue.utils;

import com.hazelcast.config.Config;

import java.util.Map;

/**
 * Component will look for an instance of this class in order to create a new HazelcastInstance
 */
public class HzConfigRegistry {

    private final Map<String, Config> hzConfigMap;

    public HzConfigRegistry(Map<String, Config> hzConfigMap) {
        this.hzConfigMap = hzConfigMap;
    }

    public Config getFor(String hzInstanceName) {
        return hzConfigMap.get(hzInstanceName);
    }

    public void register(String hzInstanceName, Config config) {
        this.hzConfigMap.put(hzInstanceName, config);
    }

}
