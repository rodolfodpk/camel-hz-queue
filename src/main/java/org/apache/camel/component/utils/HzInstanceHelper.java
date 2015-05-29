package org.apache.camel.component.utils;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class HzInstanceHelper {

    private static final Logger log = LoggerFactory.getLogger(HzInstanceHelper.class);

    private final String hzInstanceName;
    private final Optional<HzInstanceRegistry> hzInstanceRegistry;
    private final Optional<HzConfigRegistry> hzConfigRegistry;

    public HzInstanceHelper(String hzInstanceName, Optional<HzInstanceRegistry> hzInstanceRegistry, Optional<HzConfigRegistry> hzConfigRegistry) {
        this.hzInstanceName = hzInstanceName;
        this.hzInstanceRegistry = hzInstanceRegistry;
        this.hzConfigRegistry = hzConfigRegistry;
    }

    public HazelcastInstance findHzInstance() {
        if (hzInstanceRegistry.isPresent()){
            return findOrCreateHzInstance();
        } else {
            return createHzInstance();
        }
    }

    public HazelcastInstance findOrCreateHzInstance() {
        HazelcastInstance hz = hzInstanceRegistry.isPresent() ? hzInstanceRegistry.get().getHzFor(hzInstanceName) : createHzInstance();
        if (hz == null ) {
            hz = createHzInstance();
        }
        return hz;
    }

    public HazelcastInstance createHzInstance() {
        HazelcastInstance hz ;
        if (hzConfigRegistry.isPresent()) {
            final Config config = hzConfigRegistry.get().getFor(hzInstanceName);
            if (config == null) {
                hz =  Hazelcast.getHazelcastInstanceByName(hzInstanceName);
                if (hz == null) {
                    hz = Hazelcast.newHazelcastInstance();
                }
                hzConfigRegistry.get().register(hzInstanceName, hz.getConfig());
            } else {
                hz = Hazelcast.newHazelcastInstance(config);
            }
        } else {
            hz =  Hazelcast.getHazelcastInstanceByName(hzInstanceName);
            if (hz == null) {
                hz = Hazelcast.newHazelcastInstance();
            }
        }
        log.warn("Created hzInstance hzInstanceName = {} hz name = {}", hzInstanceName, hz.getName());
        hzInstanceRegistry.get().register(hzInstanceName, hz);
        return hz;
    }

}
