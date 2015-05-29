package org.apache.camel.component.util;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.main.Main;

public class Start {

    Main main;
    SimpleRegistry registry;
    CamelContext context;

    public Start() throws Exception {

        main = new Main();
        main.enableHangupSupport();
        registry = new SimpleRegistry();
        context = new DefaultCamelContext(registry);
        context.setStreamCaching(true);
        context.setUseMDCLogging(true);
        context.addRoutes(createRouteBuilder());
    }

    public static void main(String[] args) throws Exception {
        Start start = new Start();
        start.go();

        start.context.createProducerTemplate().sendBody("hz-queue://foo", "Hello!"); // send to default hz (auto created if not found on hzInstanceRegistry)

    }

    private void go() throws Exception {
        main.getCamelContexts().clear();
        main.getCamelContexts().add(context);
        main.setDuration(-1);
        main.run();
    }

    private RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("hz-queue://foo")
                        .log(LoggingLevel.INFO, "body--> ${body}")
                        .to("hz-queue://bar")
                        .to("mock:result");
            }
        };
    }

}

