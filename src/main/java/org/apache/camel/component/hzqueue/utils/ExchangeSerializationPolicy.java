package org.apache.camel.component.hzqueue.utils;

public enum ExchangeSerializationPolicy {
    JUST_BODY,
    BODY_AND_HEADERS,
    BODY_HEADERS_AND_PROPERTIES
}
