package space.x9x.radp.extension.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a URL with parameters for extension loading and configuration.
 * This class is used in the extension system to pass configuration parameters
 * to extension implementations.
 *
 * @author x9x
 * @since 2024-09-25 00:29
 */
@RequiredArgsConstructor
public class URL implements Serializable {
    private static final long serialVersionUID = 6346161581235040323L;

    @Getter
    private final Map<String, String> parameters;

    /**
     * Gets the value of a parameter by its key.
     *
     * @param key the key of the parameter to retrieve
     * @return the value of the parameter, or null if the parameter does not exist
     */
    public String getParameter(String key) {
        return parameters.get(key);
    }

}
