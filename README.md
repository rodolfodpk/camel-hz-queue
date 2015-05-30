[![Build Status](https://travis-ci.org/rodolfodpk/camel-hz-queue.svg?branch=master)](https://travis-ci.org/rodolfodpk/camel-hz-queue)

# camel-hz-queue

Camel component to consume Hazelcast queues using a ScheduledPollConsumer

## Example

```
HzInstanceRegistry hzRegistry = new HzInstanceRegistry("hz-source-cluster", Hazelcast.newHazelcastInstance());
hzRegistry.register("hz-target-cluster", Hazelcast.newHazelcastInstance());

JndiRegistry registry = new JndiRegistry();
registry.bind(HzInstanceRegistry.class.getName(), hzInstanceRegistry);

CamelContext context = new DefaultCamelContext(registry);

context.addRoutes( new RouteBuilder() {
  public void configure() {
      from("hz-queue://foo?concurrentConsumers=10&hzInstanceName=hz-source-cluster")
              .to("hz-queue://bar?hzInstanceName=hz-target-cluster")
              .to("mock:result");
  }
});

ProducerTemplate producer = context.createProducerTemplate();

producer.sendBody("hz-queue://foo?hzInstanceName=hz-source-cluster", "Hello!"); 

Main main = new org.apache.camel.main.Main();
main.getCamelContexts().clear();
main.getCamelContexts().add(context);
main.setDuration(-1);
main.run();
```

This will consume the "Hello!" message from queue foo from hz-source-cluster and send it to queue bar within hz-target-cluster.

## HZQueue Endpoint options

Option                     | Type     | Default       | Description
---------------------------|----------|---------------|------------
hzInstanceName             | String   | hz-instance-0 | Hazelcast instance             
concurrentConsumers        | int      | 10            | Concurrent consumers within ScheduledExecutorService
backoffIdleThreshold       | int      | 3             | Idle threshold
backoffErrorThreshold      | int      | 3             | Error threshold
backoffMultiplier          | int      | 3             | Number of polls that will be skipped in case of idle or error
poolingInterval            | int      | 1000          | In milliseconds
idleBackoffEventConsumer   | String   | empty string  | When an idle threshold applies, this endpoint will be notified
errorBackoffEventConsumer  | String   | empty string  | When an error threshold applies, this endpoint will be notified
transacted                 | String   | false         | Ignored. It was added just for backward compatibility

