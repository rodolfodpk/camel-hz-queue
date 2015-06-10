package org.apache.camel.component.hzqueue.utils;

import org.apache.camel.Exchange;

import java.util.Map;

public class HzComponentHelper {

    public static void copyHeaders(Exchange ex) {
        // get in headers
        Map<String, Object> headers = ex.getIn().getHeaders();

        // propagate headers if OUT message created
        if (ex.hasOut()) {
            ex.getOut().setHeaders(headers);
        }
    }
}
