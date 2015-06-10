package org.apache.camel.component.util;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.test.HazelcastTestSupport;
import org.apache.camel.component.hzqueue.utils.HzInstanceRegistry;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HzQueueTestSupport extends CamelTestSupport {

    protected HzInstanceRegistry hzInstanceRegistry;

    public HzQueueTestSupport() {
        this.hzInstanceRegistry = hzInstanceRegistry();
    }

    public HzInstanceRegistry hzInstanceRegistry() {
        return new HzInstanceRegistry(hazelcastInstance4Test());
    }

    public JndiRegistry createRegistry() {
        JndiRegistry registry = new JndiRegistry();
        registry.bind(HzInstanceRegistry.class.getName(), hzInstanceRegistry);
        return registry;
    }

    public static HazelcastInstance hazelcastInstance4Test() {
        HazelcastTestSupport hts = new HazelcastTestSupport() {
            @Override
            public HazelcastInstance createHazelcastInstance(Config config) {
                config.setProperty("hazelcast.shutdownhook.enabled", "false");
                return super.createHazelcastInstance(config);
            }
        };
        return hts.createHazelcastInstance();
    }

    protected List<UUID> generate(int dataSetSize) {
        List<UUID> result = new ArrayList<>(dataSetSize);
        for (int i=0; i< dataSetSize; i++){
            result.add(UUID.randomUUID());
            // System.out.println(result.get(i));
        }
        return result;
    }
}


