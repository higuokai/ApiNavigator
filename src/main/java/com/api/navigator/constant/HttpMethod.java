package com.api.navigator.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum HttpMethod {

    GET, POST, PUT, DELETE, PATCH, HEAD, REQUEST, UNDEFINED;

    private static final Map<String, HttpMethod> methodMap = Arrays.stream(HttpMethod.values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static HttpMethod fromMethod(String method) {
        if (method == null || method.isEmpty()) {
            return REQUEST;
        }
        return methodMap.getOrDefault(method.toUpperCase(), UNDEFINED);
    }

}
