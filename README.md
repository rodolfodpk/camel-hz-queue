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

This will consume a message from queue foo from hz-source-cluster and send it to queue bar within hz-target-cluster.





