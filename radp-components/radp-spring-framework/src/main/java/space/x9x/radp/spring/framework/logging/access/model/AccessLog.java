package space.x9x.radp.spring.framework.logging.access.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author IO x9x
 * @since 2024-09-30 09:54
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessLog {
    private String location;
    private String arguments;
    private String returnValue;
    private Throwable throwable;
    private long duration;
    private transient Instant start;
    private transient Instant end;
}
