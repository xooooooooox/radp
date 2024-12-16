package com.x9x.radp.extension.common;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author x9x
 * @since 2024-09-25 00:29
 */
@RequiredArgsConstructor
public class URL implements Serializable {
    private static final long serialVersionUID = 6346161581235040323L;

    private final Map<String, String> parameters;

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
